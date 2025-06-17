import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

class TestJsonUnpack
{
    static class Unpacked
    {
        boolean success;

//        public boolean isSuccess()
//        {
//            return success;
//        }
//
//        public void setSuccess(boolean success)
//        {
//            this.success = success;
//        }
    }
    @Test
    void test() throws Exception
    {
        var json = """
                                       {
                          "success": true,
                          "challenge_ts": "2025-06-11T15:23:08Z",
                          "hostname": "localhost"
                        }
                """;
        var mapper = new JsonMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var obj = mapper.readValue(json, Unpacked.class);
        assertThat(obj.success);
    }
}
