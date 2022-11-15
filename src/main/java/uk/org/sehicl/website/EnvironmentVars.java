package uk.org.sehicl.website;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageOptions;

public class EnvironmentVars
{
    private static final Logger LOG = LoggerFactory.getLogger(EnvironmentVars.class);

    private final Map<String, String> data;

    public EnvironmentVars()
    {
        Map<String, String> d;
        try
        {
            Blob envBlob = StorageOptions
                    .getDefaultInstance()
                    .getService()
                    .get("sehicl-website-env")
                    .get("env.yml");
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            d = mapper
                    .readValue(envBlob.getContent(), new TypeReference<HashMap<String, String>>()
                    {
                    });
        }
        catch (Exception e)
        {
            d = new HashMap<>();
            LOG.error("Unable to get environment data from cloud storage", e);
        }
        data = d;
    }

    public String get(String key)
    {
        return Optional.ofNullable(System.getenv(key)).orElseGet(() -> data.get(key));
    }
}
