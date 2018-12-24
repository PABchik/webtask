package com.Paul.web.app.entity.serializer;

import com.Paul.web.app.entity.AnswerOption;
import com.Paul.web.app.entity.Task;
import com.Paul.web.app.service.UserService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TaskSerializer extends JsonSerializer{

    @Autowired
    UserService userService;

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("taskId", ((Task)o).getId());
        jsonGenerator.writeObjectField("task Text", ((Task)o).getTaskText());


            jsonGenerator.writeArrayFieldStart("answer options");
//            jsonGenerator.writeStartArray();
            for (AnswerOption ansOpt : ((Task)o).getAnswerOptions()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeObjectField("answer option id", ansOpt.getId());
                jsonGenerator.writeObjectField("answer option", ansOpt.getAnswer());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
