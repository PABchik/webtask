package com.Paul.web.app.entity.serializer;

import com.Paul.web.app.entity.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserSerializer extends JsonSerializer {


    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        System.out.print(o.getClass());
        String str = o.getClass().toString();
        if (o instanceof User) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeObjectField("userId", ((User) o).getId());
            jsonGenerator.writeObjectField("email", ((User) o).getEmail());
            jsonGenerator.writeObjectField("firstname", ((User) o).getFirstname());
            jsonGenerator.writeObjectField("lastname", ((User) o).getLastname());
            jsonGenerator.writeEndObject();
        }
    }
}
