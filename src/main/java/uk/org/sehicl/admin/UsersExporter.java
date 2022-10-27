package uk.org.sehicl.admin;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import uk.org.sehicl.website.users.UserManager;

public class UsersExporter
{
    public final UserManager userManager;

    public UsersExporter(UserManager userManager)
    {
        this.userManager = userManager;
    }

    public String export(String adminSecret) throws IOException
    {
        if (!Objects.equals(System.getenv("ADMIN_SECRET"), adminSecret))
            return "Not authorised";
        StringWriter writer = new StringWriter();
        try (StringWriter sw = writer)
        {
            new ObjectMapper(new YAMLFactory())
                    .writeValue(sw, userManager.allUsers().collect(Collectors.toList()));
        }
        return writer.toString();
    }

}
