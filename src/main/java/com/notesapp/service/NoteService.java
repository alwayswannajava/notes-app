package com.notesapp.service;

import com.notesapp.domain.Note;
import java.util.Map;

public interface NoteService {
    Note createNote(Note note);

    Note updateNote(String noteId, Note note);

    void deleteNote(String noteId);

    Note fetchById(String noteId);

    Map<String, Long> fetchUniqueWords(Note note);
}
