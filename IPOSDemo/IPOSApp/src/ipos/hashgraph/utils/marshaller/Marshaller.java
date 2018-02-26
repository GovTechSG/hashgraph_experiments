package ipos.hashgraph.utils.marshaller;

public interface Marshaller {

    <T> String marshalStrict(T object);

    <T> String marshalLenient(T object);

    <T> T unmarshalStrict(String content, Class<T> clss);

    <T> T unmarshalLenient(String content, Class<T> clss);

}