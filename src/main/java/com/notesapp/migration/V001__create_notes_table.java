package com.notesapp.migration;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.data.mongodb.core.validation.Validator;

@ChangeUnit(id = "V001__create_notes_table", order = "1", author = "Mykhailo Kornukh")
public class V001__create_notes_table {

    @Execution
    public void createNotesCollection(MongoTemplate mongoTemplate) {
        MongoJsonSchema schema = MongoJsonSchema.builder()
                .required("title", "text")
                .properties(
                        JsonSchemaProperty.string("title").minLength(2).maxLength(255),
                        JsonSchemaProperty.date("created_date"),
                        JsonSchemaProperty.string("text").minLength(2).maxLength(255),
                        JsonSchemaProperty.array("tags"))
                .build();

        mongoTemplate.createCollection("notes",
                CollectionOptions.empty()
                        .validator(Validator.schema(schema)));
    }

    @RollbackExecution
    public void rollbackUsersCollection(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection("notes");
    }
}
