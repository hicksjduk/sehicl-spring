package uk.org.sehicl.website.users.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;

import uk.org.sehicl.website.users.PasswordReset;
import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.UserDatastore;

public class GoogleCloudDatastore implements UserDatastore
{
    private static enum Prefix
    {
        EMAIL,
        USERID,
        SESSIONID,
        SESSIONUSER,
        PWRESET;

        public String toString()
        {
            return String.format("%s-", name().toLowerCase());
        }

        public String key(String value)
        {
            return String.format("%s%s", this, value);
        }

        public String key(long value)
        {
            return String.format("%s%d", this, value);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudDatastore.class);

    private Bucket usersBucket(Storage storage)
    {
        return storage.get("sehicl-users");
    }

    private Storage storage()
    {
        return StorageOptions.getDefaultInstance().getService();
    }

    private <T> String toYaml(T obj)
    {
        try
        {
            return new ObjectMapper(new YAMLFactory()).writeValueAsString(obj);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    private <T> Function<Blob, T> fromBlob(Class<T> type)
    {
        return blob ->
        {
            if (blob == null)
                return null;
            try
            {
                return new ObjectMapper(new YAMLFactory()).readValue(blob.getContent(), type);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        };
    }

    @Override
    public User getUserByEmail(String email)
    {
        return fromBlob(User.class).apply(usersBucket(storage()).get(Prefix.EMAIL.key(email)));
    }

    @Override
    public User getUserById(long id)
    {
        return fromBlob(User.class).apply(usersBucket(storage()).get(Prefix.USERID.key(id)));
    }

    @Override
    public Collection<Long> getAllUserIds()
    {
        int prefixLength = Prefix.USERID.toString().length();
        return StreamSupport
                .stream(usersBucket(storage())
                        .list(BlobListOption.prefix(Prefix.USERID.toString()))
                        .iterateAll()
                        .spliterator(), false)
                .map(Blob::getName)
                .map(s -> s.substring(prefixLength))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    @Override
    public SessionData getSessionByUserId(long id)
    {
        return Optional
                .ofNullable(fromBlob(SessionData.class)
                        .apply(usersBucket(storage()).get(Prefix.SESSIONUSER.key(id))))
                .filter(Predicate.not(expired(SessionData::getExpiry)))
                .orElse(null);
    }

    @Override
    public SessionData getSessionBySessionId(long id)
    {
        return Optional
                .ofNullable(fromBlob(SessionData.class)
                        .apply(usersBucket(storage()).get(Prefix.SESSIONID.key(id))))
                .filter(Predicate.not(expired(SessionData::getExpiry)))
                .orElse(null);
    }

    @Override
    public SessionData setSession(User user)
    {
        final long expiry = new Date().getTime() + TimeUnit.DAYS.toMillis(1);
        SessionData answer = getSessionByUserId(user.getId());
        if (answer == null)
        {
            answer = new SessionData(getSessionId(user), user.getId(), expiry);
        }
        else
        {
            answer.setExpiry(expiry);
        }
        Bucket bucket = usersBucket(storage());
        byte[] data = toYaml(answer).getBytes();
        bucket.create(Prefix.SESSIONID.key(answer.getId()), data);
        bucket.create(Prefix.SESSIONUSER.key(answer.getUserId()), data);
        return answer;
    }

    long getSessionId(User user)
    {
        long factor = 10000000000L;
        final long time = new Date().getTime();
        long answer = user.getId() * factor + time % factor;
        return answer;
    }

    @Override
    public void clearExpiredSessions()
    {
        Storage storage = storage();
        Bucket bucket = usersBucket(storage);
        BlobId[] toDelete = StreamSupport
                .stream(bucket.list(BlobListOption.prefix("session")).iterateAll().spliterator(),
                        false)
                .filter(expired(fromBlob(SessionData.class), SessionData::getExpiry))
                .map(Blob::getBlobId)
                .toArray(BlobId[]::new);
        if (toDelete.length > 1)
            storage.delete(toDelete);
    }

    @Override
    public User createUser(User user)
    {
        long nextId = getAllUserIds().stream().max(Long::compare).orElse(-1L) + 1;
        user.setId(nextId);
        Bucket bucket = usersBucket(storage());
        byte[] data = toYaml(user).getBytes();
        bucket.create(Prefix.USERID.key(nextId), data);
        bucket.create(Prefix.EMAIL.key(user.getEmail()), data);
        return user;
    }

    @Override
    public void updateUser(User user)
    {
        Bucket bucket = usersBucket(storage());
        byte[] data = toYaml(user).getBytes();
        bucket.create(Prefix.USERID.key(user.getId()), data);
        bucket.create(Prefix.EMAIL.key(user.getEmail()), data);
    }

    @Override
    public PasswordReset generatePasswordReset(String email)
    {
        PasswordReset answer = null;
        final User user = getUserByEmail(email);
        if (user != null)
        {
            answer = new PasswordReset(user.getId(), email);
            usersBucket(storage())
                    .create(Prefix.PWRESET.key(answer.getId()), toYaml(answer).getBytes());
        }
        return answer;
    }

    @Override
    public PasswordReset getPasswordReset(long id)
    {
        return Optional
                .ofNullable(fromBlob(PasswordReset.class)
                        .apply(usersBucket(storage()).get(Prefix.PWRESET.key(id))))
                .filter(Predicate.not(expired(PasswordReset::getExpiry)))
                .orElse(null);
    }

    private final <T> Predicate<Blob> expired(Function<Blob, T> extractor,
            ToLongFunction<T> expiryGetter)
    {
        return blob -> expired(expiryGetter).test(extractor.apply(blob));
    }

    private final <T> Predicate<T> expired(ToLongFunction<T> expiryGetter)
    {
        return obj -> expiryGetter.applyAsLong(obj) < new Date().getTime();
    }

    @Override
    public void clearExpiredResets()
    {
        Bucket bucket = usersBucket(storage());
        BlobId[] toDelete = StreamSupport
                .stream(bucket
                        .list(BlobListOption.prefix(Prefix.PWRESET.toString()))
                        .iterateAll()
                        .spliterator(), false)
                .filter(expired(fromBlob(PasswordReset.class), PasswordReset::getExpiry))
                .map(Blob::getBlobId)
                .toArray(BlobId[]::new);
        if (toDelete.length > 0)
            storage().delete(toDelete);
    }

    @Override
    public void deleteUser(long id)
    {
        // TODO Auto-generated method stub
        // Not for now
    }

}
