package com.Paul.web.app.entity.serializer;

import com.Paul.web.app.entity.Test;
import com.Paul.web.app.service.UserService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TestSerializer extends JsonSerializer {



    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("testId", ((Test)o).getId());
        jsonGenerator.writeObjectField("name", ((Test)o).getName());
        jsonGenerator.writeEndObject();
    }
}
