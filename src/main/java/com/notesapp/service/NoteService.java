package com.notesapp.service;

import com.notesapp.domain.Note;
import com.notesapp.domain.NoteType;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NoteService {
    Note createNote(Note note);

    Note updateNote(String noteId, Note note);

    void deleteNote(String noteId);

    Note fetchById(String noteId);

    Map<String, Long> fetchUniqueWords(Note note);

    List<Note> fetchAllNotes(Set<NoteType> filters, Pageable pageable);
}
