package com.notesapp.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notes")
public class Note {

    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "created_date")
    private LocalDateTime createdDate;

    @Field(name = "text")
    private String text;

    @Field(name = "type")
    private NoteType type;
}
