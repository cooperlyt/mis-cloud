package io.github.cooperlyt.commons.cloud.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


/**
 * usage @JsonSerialize(using = JsonRawSerialize.class)
 *
 * @deprecated use {@link com.fasterxml.jackson.annotation.JsonRawValue} instead
 */
@Deprecated
public class JsonRawSerialize extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeRawValue(s);

    }


}
