package org.akriuchk.minishop.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.akriuchk.minishop.model.Image;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class ImageToUrlSerializer extends StdSerializer<Set<Image>> {
    public ImageToUrlSerializer() {
        this(null);
    }

    public ImageToUrlSerializer(Class<Set<Image>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<Image> images, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for (Image image : images) {
            long id = image.getId();
            try {
                jsonGenerator.writeObject(id);

            } catch (IOException e) {
                log.error("Error in image serialisation to json writing: {}", image.getId(), e);
            }
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.flush();
    }
}
