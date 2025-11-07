package com.notesapp.repository;

import com.notesapp.domain.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> {

}
