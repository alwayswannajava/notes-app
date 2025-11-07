package com.notesapp;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongock
@EnableMongoRepositories
@EnableMongoAuditing
public class NotesAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesAppApplication.class, args);
    }

}
