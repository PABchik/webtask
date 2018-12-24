package com.Paul.web.app.entity.serializer;

import com.Paul.web.app.entity.Organisation;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrganisationSerializer extends JsonSerializer{

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("organisationId", ((Organisation)o).getId());
        jsonGenerator.writeObjectField("organisationName", ((Organisation)o).getName());
        jsonGenerator.writeEndObject();
    }
}
