package com.notesapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "notes")
public class Note {
    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    @Field(name = "text")
    private String text;

    @Field(name = "tags")
    private Set<String> tags;
}
