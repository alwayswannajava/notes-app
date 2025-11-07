package com.notesapp.service.impl;

import com.notesapp.domain.Note;
import com.notesapp.repository.NoteRepository;
import com.notesapp.service.NoteService;
import com.notesapp.service.exception.NoteNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService {
    private static final String WORD_SPLIT_REGEX = "\\W+";

    private final NoteRepository noteRepository;

    @Override
    public Note createNote(Note note) {
        log.info("Trying to create a node with data: {}", note);
        var savedNote = noteRepository.save(note);
        log.info("Note created successfully, id: {}", savedNote);
        return savedNote;
    }

    @Override
    public Note updateNote(String noteId, Note note) {
        log.info("Trying to update a note with id: {}", noteId);
        return null;
    }

    @Override
    public void deleteNote(String noteId) {
        log.info("Trying to delete a node with id: {}", noteId);
        noteRepository.deleteById(noteId);
        log.info("Note with id: {} deleted successfully", noteId);
    }

    @Override
    public Note fetchById(String noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(
                        () -> new NoteNotFoundException("Note with id: " + noteId + " not found"));
    }

    @Override
    public Map<String, Long> fetchUniqueWords(Note note) {
        return Stream.of(splitNoteTextByWordSplitPattern(note.getText()))
                .filter(word -> !word.isBlank())
                .collect(groupingBy(identity(), counting()));
    }

    private String[] splitNoteTextByWordSplitPattern(String text) {
        return text.split(WORD_SPLIT_REGEX);
    }
}
