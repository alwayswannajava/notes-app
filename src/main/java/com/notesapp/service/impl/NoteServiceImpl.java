package com.notesapp.service.impl;

import com.notesapp.domain.Note;
import com.notesapp.domain.NoteType;
import com.notesapp.repository.NoteRepository;
import com.notesapp.service.NoteService;
import com.notesapp.service.exception.NoteNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        //TODO: update
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
        log.info("Fetching note with id: {}", noteId);
        return noteRepository.findById(noteId)
                .orElseThrow(
                        () -> new NoteNotFoundException("Note with id: " + noteId + " not found"));
    }

    @Override
    public Map<String, Long> fetchUniqueWords(Note note) {
        log.info("Fetching unique words from note with id: {}", note.getId());
        return Stream.of(splitNoteTextByWordSplitPattern(note.getText()))
                .filter(word -> !word.isBlank())
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByKey().reversed())
                .collect(toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public List<Note> fetchAllNotes(Set<NoteType> filters, Pageable pageable) {
        log.info("Fetching all notes with filters: {}, pageable: {}", filters, pageable);
        //TODO: mapping to fetch response
        return filters == null ? noteRepository.findAll(pageable).getContent()
                                   : noteRepository.findAllByTags(filters, pageable);
    }

    private String[] splitNoteTextByWordSplitPattern(String text) {
        return text.split(WORD_SPLIT_REGEX);
    }
}
