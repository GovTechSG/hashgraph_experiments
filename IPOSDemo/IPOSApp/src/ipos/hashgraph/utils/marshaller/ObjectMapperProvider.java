package ipos.hashgraph.utils.marshaller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

    ObjectMapper defaultObjectMapper = createDefaultMapper();

    @Override
    public ObjectMapper getContext(Class aClass) {
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL).disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        result.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        result.configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        return result;
    }

}