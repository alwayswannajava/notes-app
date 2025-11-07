package com.notesapp.repository;

import com.notesapp.domain.Note;
import com.notesapp.domain.NoteType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Set;

public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findAllByTags(Set<NoteType> tags, Pageable pageable);
}
