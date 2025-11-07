package com.notesapp.controller;

import com.notesapp.controller.mapper.NoteMapper;
import com.notesapp.domain.Note;
import com.notesapp.domain.NoteType;
import com.notesapp.dto.request.CreateNoteRequest;
import com.notesapp.dto.request.UpdateNoteRequest;
import com.notesapp.dto.response.CreateNoteResponse;
import com.notesapp.dto.response.UpdateNoteResponse;
import com.notesapp.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notes")
@Validated
public class NoteController {
    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreateNoteResponse> createNote(@Valid @RequestBody CreateNoteRequest request) {
        log.info("---------------------------POST REQUEST---------------------------");
        Note createdNote = noteService.createNote(noteMapper.toNote(request));
        log.info("---------------------------POST REQUEST END---------------------------");
        return ResponseEntity.ok(noteMapper.toResponse(createdNote));
    }

    @PatchMapping("/update/{noteId}")
    public ResponseEntity<UpdateNoteResponse> updateNote(@PathVariable String noteId,
            @Valid @RequestBody UpdateNoteRequest request) {
        log.info("---------------------------PATCH REQUEST---------------------------");
        var note = noteService.fetchById(noteId);
        log.info("---------------------------PATCH REQUEST END---------------------------");
        return null;
    }

    @DeleteMapping("/delete/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable String noteId) {
        log.info("---------------------------DELETE REQUEST---------------------------");
        noteService.deleteNote(noteId);
        log.info("---------------------------DELETE REQUEST END---------------------------");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{noteId}/stats")
    public ResponseEntity<Map<String, Long>> fetchUniqueWords(@PathVariable String noteId) {
        log.info("-----------------------------GET REQUEST---------------------------");
        Note note = noteService.fetchById(noteId);
        Map<String, Long> uniqueWordCountMap = noteService.fetchUniqueWords(note);
        log.info("-----------------------------GET REQUEST END---------------------------");
        return ResponseEntity.ok(uniqueWordCountMap);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<?> fetchAll(@RequestParam(required = false) Set<NoteType> noteTypes,
                                           @PageableDefault(direction = Sort.Direction.DESC,
                                                   sort = "createdDate") Pageable pageable) {
        log.info("-----------------------------GET REQUEST---------------------------");
        List<Note> notes = noteService.fetchAllNotes(noteTypes, pageable);
        log.info("-----------------------------GET REQUEST END---------------------------");
        return ResponseEntity.ok(notes);
    }
}
