package uk.org.sehicl.website.users.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobListOption;
import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;

import uk.org.sehicl.website.users.PasswordReset;
import uk.org.sehicl.website.users.SessionData;
import uk.org.sehicl.website.users.User.Status;
import uk.org.sehicl.website.users.impl.GoogleCloudDatastore.Prefix;

public class GoogleCloudDatastoreTest
{
    private final Storage storage = LocalStorageHelper.getOptions().getService();
    private final GoogleCloudDatastore datastore = new GoogleCloudDatastore(() -> storage);

    @BeforeEach
    void clearDb()
    {
        StreamSupport
                .stream(storage.list(datastore.usersBucket).iterateAll().spliterator(), false)
                .map(Blob::getBlobId)
                .forEach(storage::delete);
    }

    @Test
    void testGetUserByIdDoesNotExist()
    {
        assertThat(datastore.getUserById(0)).isNull();
    }

    @Test
    void testGetUserByIdExists()
    {
        var user = datastore.createUser("email", "name", "club", Status.ACTIVE, "password");
        assertThat(datastore.getUserById(0)).isNotNull();
        assertThat(user.getId()).isEqualTo(0);
        assertThat(user.getEmail()).isEqualTo("email");
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getClub()).isEqualTo("club");
        assertThat(user.getEmail()).isEqualTo("email");
    }

    @Test
    void testGetUserByEmailDoesNotExist()
    {
        assertThat(datastore.getUserByEmail("rubbish")).isNull();
    }

    @Test
    void testGetUserByEmailExists()
    {
        var user = datastore.createUser("email", "name", "club", Status.ACTIVE, "password");
        assertThat(datastore.getUserByEmail(user.getEmail())).isNotNull();
        assertThat(user.getId()).isEqualTo(0);
        assertThat(user.getEmail()).isEqualTo("email");
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getClub()).isEqualTo("club");
        assertThat(user.getEmail()).isEqualTo("email");
    }

    @Test
    void testGetAllUserIdsThereAreNone()
    {
        assertThat(datastore.getAllUserIds()).isEmpty();
    }

    @Test
    void testGetAllUserIdsThereAreSome()
    {
        IntStream
                .range(0, 5)
                .forEach(i -> datastore
                        .createUser("email" + i, "name", "club", Status.ACTIVE, "password"));
        datastore.deleteUser(2);
        assertThat(datastore.getAllUserIds()).contains(0L, 1L, 3L, 4L);
    }

    @Test
    void testGetSessionByUserIdNotExists()
    {
        assertThat(datastore.getSessionByUserId(0)).isNull();
    }

    @Test
    void testGetSessionBySessionIdNotExists()
    {
        assertThat(datastore.getSessionBySessionId(0)).isNull();
    }

    @Test
    void testCreateAndQuerySession()
    {
        var user = datastore.createUser("email", "name", "club", Status.ACTIVE, "password");
        var session = datastore.setSession(user);
        var sessions = Stream
                .of(datastore.getSessionByUserId(user.getId()),
                        datastore.getSessionBySessionId(session.getId()))
                .toArray(SessionData[]::new);
        assertThat(sessions.length).isEqualTo(2);
        Stream
                .of(sessions)
                .map(SessionData::getExpiry)
                .forEach(s -> assertThat(s).isEqualTo(session.getExpiry()));
    }

    @Test
    void testGetPasswordResetNotExists()
    {
        assertThat(datastore.getPasswordReset(0)).isNull();
    }

    @Test
    void testGeneratePasswordResetEmailNotKnown()
    {
        assertThat(datastore.generatePasswordReset("rubbish")).isNull();
    }

    @Test
    void testGeneratePasswordResetEmailKnown()
    {
        datastore.createUser("email", "name", "club", Status.ACTIVE, "password");
        var reset = datastore.generatePasswordReset("email");
        assertThat(reset).isNotNull();
        assertThat(datastore.getPasswordReset(reset.getId()).getExpiry())
                .isEqualTo(reset.getExpiry());
    }

    @Test
    void testClearExpiredResetsNoneExpired()
    {
        var expiries = IntStream
                .range(0, 5)
                .peek(i -> datastore
                        .createUser("email" + i, "name", "club", Status.ACTIVE, "password"))
                .mapToObj(i -> datastore.generatePasswordReset("email" + i))
                .map(PasswordReset::getExpiry)
                .toArray(Long[]::new);
        datastore.clearExpiredResets();
        assertThat(StreamSupport
                .stream(storage
                        .list(datastore.usersBucket,
                                BlobListOption.prefix(Prefix.PWRESET.toString()))
                        .iterateAll()
                        .spliterator(), false)
                .map(mapper(PasswordReset.class))
                .mapToLong(PasswordReset::getExpiry)).contains(expiries);
    }

    @Test
    void testClearExpiredResetsSomeExpired()
    {
        var resets = IntStream
                .range(0, 5)
                .peek(i -> datastore
                        .createUser("email" + i, "name", "club", Status.ACTIVE, "password"))
                .mapToObj(i -> datastore.generatePasswordReset("email" + i))
                .toArray(PasswordReset[]::new);
        IntStream
                .of(1, 3)
                .mapToObj(i -> resets[i])
                .peek(r -> r.setExpiry(r.getExpiry() - TimeUnit.DAYS.toMillis(2)))
                .forEach(r -> inserter(Prefix.PWRESET.key(r.getUserEmail())));
        datastore.clearExpiredResets();
        var expiries = IntStream
                .of(0, 2, 4)
                .mapToObj(i -> resets[i])
                .map(PasswordReset::getExpiry)
                .toArray(Long[]::new);
        assertThat(StreamSupport
                .stream(storage
                        .list(datastore.usersBucket,
                                BlobListOption.prefix(Prefix.PWRESET.toString()))
                        .iterateAll()
                        .spliterator(), false)
                .map(mapper(PasswordReset.class))
                .mapToLong(PasswordReset::getExpiry)).contains(expiries);
    }

    @Test
    void testClearExpiredSessionsNoneExpired()
    {
        var expiries = IntStream
                .range(0, 5)
                .mapToObj(i -> datastore
                        .createUser("email" + i, "name", "club", Status.ACTIVE, "password"))
                .map(datastore::setSession)
                .map(SessionData::getExpiry)
                .toArray(Long[]::new);
        datastore.clearExpiredSessions();
        assertThat(StreamSupport
                .stream(storage
                        .list(datastore.usersBucket,
                                BlobListOption.prefix(Prefix.SESSIONID.toString()))
                        .iterateAll()
                        .spliterator(), false)
                .map(mapper(SessionData.class))
                .mapToLong(SessionData::getExpiry)).contains(expiries);
    }

    @Test
    void testClearExpiredSessionsSomeExpired()
    {
        var sessions = IntStream
                .range(0, 5)
                .mapToObj(i -> datastore
                        .createUser("email" + i, "name", "club", Status.ACTIVE, "password"))
                .map(datastore::setSession)
                .toArray(SessionData[]::new);
        IntStream
                .of(1, 3)
                .mapToObj(i -> sessions[i])
                .peek(s -> s.setExpiry(s.getExpiry() - TimeUnit.DAYS.toMillis(2)))
                .forEach(s -> inserter(Prefix.SESSIONID.key(s.getId())));
        datastore.clearExpiredResets();
        var expiries = IntStream
                .of(0, 2, 4)
                .mapToObj(i -> sessions[i])
                .map(SessionData::getExpiry)
                .toArray(Long[]::new);
        assertThat(StreamSupport
                .stream(storage
                        .list(datastore.usersBucket,
                                BlobListOption.prefix(Prefix.SESSIONID.toString()))
                        .iterateAll()
                        .spliterator(), false)
                .map(mapper(PasswordReset.class))
                .mapToLong(PasswordReset::getExpiry)).contains(expiries);
    }

    @Test
    void testUpdateUser()
    {
        var user = datastore.createUser("email", "name", "club", Status.INACTIVE, "password");
        user.setStatus(Status.ACTIVE);
        datastore.updateUser(user);
        assertThat(datastore.getUserById(user.getId()).getStatus()).isEqualTo(Status.ACTIVE);
    }

    <T> Function<Blob, T> mapper(Class<T> type)
    {
        return b ->
        {
            try
            {
                return new ObjectMapper(new YAMLFactory()).readValue(b.getContent(), type);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        };
    }

    <T> Consumer<T> inserter(String key)
    {
        return o ->
        {
            try
            {
                storage
                        .create(BlobInfo.newBuilder(datastore.usersBucket, key).build(),
                                new ObjectMapper(new YAMLFactory()).writeValueAsBytes(o));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        };
    }
}
