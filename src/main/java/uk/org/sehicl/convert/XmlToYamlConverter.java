package uk.org.sehicl.convert;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import uk.org.sehicl.website.contacts.Contacts;

public class XmlToYamlConverter
{

    public static void main(String[] args) throws Exception
    {
        convert(Paths.get("src/main/resources/data/contacts.xml"), Contacts.class);
    }

    static <T> void convert(Path file, Class<T> objType) throws Exception
    {
        T data = new XmlMapper().readValue(file.toFile(), objType);
        new YAMLMapper()
                .writeValue(file
                        .resolveSibling(file.getFileName().toString().split("\\.")[0] + ".yml")
                        .toFile(), data);
    }
}
