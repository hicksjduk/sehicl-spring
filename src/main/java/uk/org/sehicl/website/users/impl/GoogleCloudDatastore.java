package uk.org.sehicl.website.users.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;

import uk.org.sehicl.website.users.PasswordReset;
import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.UserDatastore;

public class GoogleCloudDatastore implements UserDatastore
{
    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudDatastore.class);

    private static String usersBucket()
    {
        return Optional.of("USERS_BUCKET").map(System::getenv).orElse("sehicl-users");
    }

    static enum Prefix
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

    private static Supplier<Storage> createStorageGetter()
    {
        return Optional.of("LOCAL_DATASTORE").map(System::getenv).map(s ->
        {
            var storage = LocalStorageHelper.getOptions();
            fromFile(s).forEach(new GoogleCloudDatastore(storage::getService)::createUser);
            return storage;
        }).orElseGet(() -> StorageOptions.getDefaultInstance())::getService;
    }

    private static Stream<User> fromFile(String fileName)
    {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            ArrayType type = mapper.getTypeFactory().constructArrayType(User.class);
            User[] array = (User[]) mapper.readValue(br, type);
            return Stream.of(array);
        }
        catch (FileNotFoundException ex)
        {
            return Stream.empty();
        }
        catch (Exception ex)
        {
            throw new RuntimeException("Unable to load users from file", ex);
        }
    }

    private final Supplier<Storage> storageGetter;
    public final String usersBucket;

    public GoogleCloudDatastore()
    {
        this(createStorageGetter());
    }

    public GoogleCloudDatastore(Supplier<Storage> storageGetter)
    {
        this.storageGetter = storageGetter;
        this.usersBucket = usersBucket();
    }

    private Storage storage()
    {
        return storageGetter.get();
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
        return fromBlob(User.class).apply(storage().get(usersBucket, Prefix.EMAIL.key(email)));
    }

    @Override
    public User getUserById(long id)
    {
        return fromBlob(User.class).apply(storage().get(usersBucket, Prefix.USERID.key(id)));
    }

    @Override
    public Collection<Long> getAllUserIds()
    {
        int prefixLength = Prefix.USERID.toString().length();
        return StreamSupport
                .stream(storage()
                        .list(usersBucket, BlobListOption.prefix(Prefix.USERID.toString()))
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
                        .apply(storage().get(usersBucket, Prefix.SESSIONUSER.key(id))))
                .filter(Predicate.not(expired(SessionData::getExpiry)))
                .orElse(null);
    }

    @Override
    public SessionData getSessionBySessionId(long id)
    {
        return Optional
                .ofNullable(fromBlob(SessionData.class)
                        .apply(storage().get(usersBucket, Prefix.SESSIONID.key(id))))
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
        byte[] data = toYaml(answer).getBytes();
        Stream
                .of(Prefix.SESSIONID.key(answer.getId()),
                        Prefix.SESSIONUSER.key(answer.getUserId()))
                .map(key -> BlobInfo.newBuilder(usersBucket, key))
                .map(BlobInfo.Builder::build)
                .forEach(bi -> storage().create(bi, data));
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
        BlobId[] toDelete = StreamSupport
                .stream(storage()
                        .list(usersBucket, BlobListOption.prefix("session"))
                        .iterateAll()
                        .spliterator(), false)
                .filter(expired(fromBlob(SessionData.class), SessionData::getExpiry))
                .map(Blob::getBlobId)
                .toArray(BlobId[]::new);
        if (toDelete.length > 1)
            storage().delete(toDelete);
    }

    @Override
    public User createUser(User user)
    {
        long nextId = getAllUserIds().stream().max(Long::compare).orElse(-1L) + 1;
        user.setId(nextId);
        LOG.info("Updating user {}/{}", user.getId(), user.getEmail());
        updateUser(user);
        LOG.info("User {}/{} updated", user.getId(), user.getEmail());
        return user;
    }

    @Override
    public void updateUser(User user)
    {
        byte[] data = toYaml(user).getBytes();
        Stream
                .of(Prefix.USERID.key(user.getId()), Prefix.EMAIL.key(user.getEmail()))
                .map(key -> BlobInfo.newBuilder(usersBucket, key).setContentType("text/yaml"))
                .map(BlobInfo.Builder::build)
                .forEach(bi -> storage().create(bi, data));
    }

    @Override
    public PasswordReset generatePasswordReset(String email)
    {
        PasswordReset answer = null;
        final User user = getUserByEmail(email);
        if (user != null)
        {
            answer = new PasswordReset(user.getId(), email);
            storage()
                    .create(BlobInfo
                            .newBuilder(usersBucket, Prefix.PWRESET.key(answer.getId()))
                            .build(), toYaml(answer).getBytes());
        }
        return answer;
    }

    @Override
    public PasswordReset getPasswordReset(long id)
    {
        return Optional
                .ofNullable(fromBlob(PasswordReset.class)
                        .apply(storage().get(usersBucket, Prefix.PWRESET.key(id))))
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
        Storage storage = storage();
        Optional
                .of(StreamSupport
                        .stream(storage
                                .list(usersBucket, BlobListOption.prefix(Prefix.PWRESET.toString()))
                                .iterateAll()
                                .spliterator(), false)
                        .filter(expired(fromBlob(PasswordReset.class), PasswordReset::getExpiry))
                        .map(Blob::getBlobId)
                        .toArray(BlobId[]::new))
                .filter(a -> a.length > 0)
                .ifPresent(storage::delete);
    }

    @Override
    public void deleteUser(long id)
    {
        Stream
                .ofNullable(getUserById(id))
                .flatMap(u -> Stream
                        .of(Prefix.USERID.key(id), Prefix.SESSIONUSER.key(id),
                                Prefix.EMAIL.key(u.getEmail())))
                .map(k -> BlobId.of(usersBucket, k))
                .forEach(storage()::delete);
    }

}
