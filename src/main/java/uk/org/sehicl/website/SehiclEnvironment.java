package uk.org.sehicl.website;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.StorageOptions;

public class SehiclEnvironment
{
    public static class EnvironmentData
    {
        private String adminSecret;

        public String getAdminSecret()
        {
            return adminSecret;
        }

        @JsonProperty("ADMIN_SECRET")
        public void setAdminSecret(String adminSecret)
        {
            this.adminSecret = adminSecret;
        }
    }

    private EnvironmentData data;

    public EnvironmentData get()
    {
        if (data != null)
            return data;
        Blob envBlob = StorageOptions
                .getDefaultInstance()
                .getService()
                .get("sehicl-website-env")
                .get("env.yml");
        try
        {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            data = mapper.readValue(envBlob.getContent(), EnvironmentData.class);
            return data;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to get environment data from cloud storage", e);
        }
    }
}
