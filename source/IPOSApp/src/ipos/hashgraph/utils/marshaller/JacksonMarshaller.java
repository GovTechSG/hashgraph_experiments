package ipos.hashgraph.utils.marshaller;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonMarshaller implements Marshaller {
    private final ObjectMapper strictObjectMapper;
    private final ObjectMapper lenientObjectMapper;

    public JacksonMarshaller() {
        this.strictObjectMapper = strictObjectMapper();
        this.lenientObjectMapper = lenientObjectMapper();
    }

    @Override
    public <T> String marshalStrict(T object) {
        try {
            return strictObjectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public <T> String marshalLenient(T object) {
        try {
            return lenientObjectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public <T> T unmarshalStrict(String content, Class<T> clss) {
        try {
            return strictObjectMapper.readValue(content, clss);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public <T> T unmarshalLenient(String content, Class<T> clss) {
        try {
            return lenientObjectMapper.readValue(content, clss);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static ObjectMapper strictObjectMapper() {
        return new ObjectMapper()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static ObjectMapper lenientObjectMapper() {
        return strictObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

}