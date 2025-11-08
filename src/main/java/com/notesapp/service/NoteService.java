package com.notesapp.service;

import com.notesapp.domain.NoteType;
import com.notesapp.dto.request.CreateNoteRequest;
import com.notesapp.dto.request.UpdateNoteRequest;
import com.notesapp.dto.response.CreateNoteResponse;
import com.notesapp.dto.response.FetchNoteResponse;
import com.notesapp.dto.response.UpdateNoteResponse;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NoteService {
    CreateNoteResponse createNote(CreateNoteRequest request);

    UpdateNoteResponse updateNote(String noteId, UpdateNoteRequest request);

    void deleteNote(String noteId);

    FetchNoteResponse fetchById(String noteId);

    Map<String, Long> fetchUniqueWords(String noteId);

    String fetchTextById(String noteId);

    List<FetchNoteResponse> fetchAllNotes(Set<NoteType> filters, Pageable pageable);
}
