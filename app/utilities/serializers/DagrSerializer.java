package utilities.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import models.dagr.Dagr;

import java.io.IOException;

public class DagrSerializer extends StdSerializer<Dagr> {

    public DagrSerializer(Class<Dagr> t) {
        super(t);
    }

    @Override
    public void serialize(Dagr value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.id);
        gen.writeStringField("dagrName", value.dagrName);
        gen.writeStringField("author", value.author);
        gen.writeStringField("documentName", value.documentName);
        gen.writeStringField("contentType", value.contentType);
        gen.writeStringField("resourceLocation", value.resourceLocation);
        gen.writeNumberField("fileSize", value.fileSize);
        gen.writeStringField("dagrUuid", value.dagrUuid.toString());
//        gen.writ
        gen.writeEndObject();
    }
}
