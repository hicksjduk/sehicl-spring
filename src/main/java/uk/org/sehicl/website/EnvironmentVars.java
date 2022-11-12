package uk.org.sehicl.website;

import java.util.HashMap;

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
    
    public EnvironmentVars()
    {
        try
        {
            Blob envBlob = StorageOptions
                    .getDefaultInstance()
                    .getService()
                    .get("sehicl-website-env")
                    .get("env.yml");
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            var data = mapper
                    .readValue(envBlob.getContent(), new TypeReference<HashMap<String, String>>()
                    {
                    });
            data.entrySet().forEach(e -> {
                if (System.getenv(e.getKey()) == null)
                    System.setProperty(e.getKey(), e.getValue());
            });
        }
        catch (Exception e)
        {
            LOG.error("Unable to get environment data from cloud storage", e);
        }

    }

}
