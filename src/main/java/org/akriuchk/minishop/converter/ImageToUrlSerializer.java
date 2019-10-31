package org.akriuchk.minishop.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.akriuchk.minishop.model.Image;

import java.io.IOException;

public class ImageToUrlSerializer extends StdSerializer<Image> {
    public ImageToUrlSerializer() {
        this(null);
    }

    public ImageToUrlSerializer(Class<Image> t) {
        super(t);
    }

    @Override
    public void serialize(Image image, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(image.getId());
    }
}
