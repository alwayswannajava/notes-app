package com.notesapp.util;

import com.notesapp.domain.Note;
import com.notesapp.domain.NoteType;
import com.notesapp.dto.request.CreateNoteRequest;
import com.notesapp.dto.request.UpdateNoteRequest;
import com.notesapp.dto.response.CreateNoteResponse;
import com.notesapp.dto.response.FetchNoteResponse;
import com.notesapp.dto.response.UpdateNoteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TestUtil {
    public static Note createNote() {
        return Note.builder()
                .id("note123")
                .text("This is a test note note")
                .tags(Set.of("BUSINESS", "PERSONAL"))
                .build();
    }

    public static Note createNoteWithText(String text) {
        return Note.builder()
                .id("note123")
                .text(text)
                .tags(Set.of("BUSINESS"))
                .build();
    }

    public static Note createUpdatedNote() {
        return Note.builder()
                .id("note123")
                .text("Updated text")
                .tags(Set.of("PERSONAL"))
                .build();
    }

    public static List<Note> createNoteList() {
        return List.of(
                Note.builder().id("1").text("Note 1").tags(Set.of("BUSINESS")).build(),
                Note.builder().id("2").text("Note 2").tags(Set.of("PERSONAL")).build()
        );
    }

    public static List<Note> createMultipleNotes() {
        return List.of(
                Note.builder().id("1").text("Note 1").build(),
                Note.builder().id("2").text("Note 2").build()
        );
    }

    public static CreateNoteRequest createCreateNoteRequest() {
        return new CreateNoteRequest(
                "New note text",
                "Create note text",
                Set.of(NoteType.BUSINESS));
    }

    public static UpdateNoteRequest createUpdateNoteRequest() {
        return new UpdateNoteRequest(
                "Update note text",
                "Update note text",
                Set.of(NoteType.PERSONAL));
    }

    public static CreateNoteResponse createCreateNoteResponse() {
        return new CreateNoteResponse("note123", LocalDateTime.now());
    }

    public static UpdateNoteResponse createUpdateNoteResponse() {
        return new UpdateNoteResponse("note123", "updated", Set.of(NoteType.PERSONAL));
    }

    public static FetchNoteResponse createFetchNoteResponse() {
        return new FetchNoteResponse(
                "note123",
                LocalDateTime.now()
        );
    }

    public static List<FetchNoteResponse> createFetchNoteResponseList() {
        return List.of(
                new FetchNoteResponse("1", LocalDateTime.now()),
                new FetchNoteResponse("2", LocalDateTime.now())
        );
    }

    public static List<FetchNoteResponse> createMultipleFetchResponses() {
        return List.of(
                new FetchNoteResponse("1", LocalDateTime.now()),
                new FetchNoteResponse("2", LocalDateTime.now())
        );
    }

    public static Pageable createPageable() {
        return PageRequest.of(0, 10);
    }

    public static Page<Note> createNotePage() {
        List<Note> notes = List.of(createNote());
        return new PageImpl<>(notes, createPageable(), notes.size());
    }

    public static Page<Note> createEmptyNotePage() {
        return new PageImpl<>(Collections.emptyList(), createPageable(), 0);
    }

    public static Page<Note> createPageWithNotes(List<Note> notes, Pageable pageable) {
        return new PageImpl<>(notes, pageable, 10);
    }
}
