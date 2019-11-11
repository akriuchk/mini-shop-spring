package org.akriuchk.minishop.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ImageContentSerializer extends StdSerializer<byte[]> {
    public ImageContentSerializer() {
        this(null);
    }

    public ImageContentSerializer(Class<byte[]> t) {
        super(t);
    }

    @Override
    public void serialize(byte[] imageContent, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(imageContent.length);
    }
}
