package uk.org.sehicl.admin;

import java.io.Reader;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import uk.org.sehicl.website.users.User;
import uk.org.sehicl.website.users.UserDatastore;

public class UsersImporter
{
    private final UserDatastore datastore;

    public UsersImporter(UserDatastore datastore)
    {
        this.datastore = datastore;
    }
    
    public int importUsers(Reader data)
    {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ArrayType type = mapper.getTypeFactory().constructArrayType(User.class);
        try
        {
            User[] array = (User[]) mapper.readValue(data, type);
            Stream.of(array).forEach(datastore::createUser);
            return array.length;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
