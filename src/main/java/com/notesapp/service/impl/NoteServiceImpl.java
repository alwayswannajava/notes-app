package com.notesapp.service.impl;

import com.notesapp.domain.NoteType;
import com.notesapp.dto.request.CreateNoteRequest;
import com.notesapp.dto.request.UpdateNoteRequest;
import com.notesapp.dto.response.CreateNoteResponse;
import com.notesapp.dto.response.FetchNoteResponse;
import com.notesapp.dto.response.UpdateNoteResponse;
import com.notesapp.repository.NoteRepository;
import com.notesapp.service.NoteService;
import com.notesapp.service.exception.NoteNotFoundException;
import com.notesapp.service.mapper.NoteMapper;
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
    private static final String WHITESPACE_SPLIT_REGEX = "\\s+";
    private static final String WORD_REGEX = "[a-zA-Z]+";

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Override
    public CreateNoteResponse createNote(CreateNoteRequest request) {
        log.info("Trying to create a node with data: {}", request);
        var savedNote = noteRepository.save(noteMapper.toNote(request));
        log.info("Note created successfully, id: {}", savedNote);
        return noteMapper.toCreateResponse(savedNote);
    }

    @Override
    public UpdateNoteResponse updateNote(String noteId, UpdateNoteRequest request) {
        log.info("Trying to update a note with id: {}", noteId);
        var note = noteRepository.findById(noteId)
                .orElseThrow(
                        () -> new NoteNotFoundException("Note with id: " + noteId + " not found"));
        var updatedNote = noteRepository.save(noteMapper.toNote(note, request));
        log.info("Note updated successfully, id: {}", updatedNote);
        return noteMapper.toUpdateResponse(updatedNote);
    }

    @Override
    public void deleteNote(String noteId) {
        log.info("Trying to delete a node with id: {}", noteId);
        noteRepository.deleteById(noteId);
        log.info("Note with id: {} deleted successfully", noteId);
    }

    @Override
    public FetchNoteResponse fetchById(String noteId) {
        log.info("Fetching note with id: {}", noteId);
        var note = noteRepository.findById(noteId)
                .orElseThrow(
                        () -> new NoteNotFoundException("Note with id: " + noteId + " not found"));
        return noteMapper.toFetchResponse(note);
    }

    @Override
    public Map<String, Long> fetchUniqueWords(String noteId) {
        log.info("Fetching unique words from note with id: {}", noteId);
        var note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note with id: " + noteId + " not found"));

        return Stream.of(splitNoteTextByWordSplitPattern(note.getText()))
                .filter(word -> !word.isBlank() && word.matches(WORD_REGEX))
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
    public String fetchTextById(String noteId) {
        log.info("Fetching text from note with id: {}", noteId);
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException("Note with id: " + noteId + " not found"))
                .getText();
    }

    @Override
    public List<FetchNoteResponse> fetchAllNotes(Set<NoteType> filters, Pageable pageable) {
        log.info("Fetching all notes with filters: {}, pageable: {}", filters, pageable);
        return filters == null ? findAll(pageable)
                               : findAllByTags(filters, pageable);
    }

    private String[] splitNoteTextByWordSplitPattern(String text) {
        return text.split(WHITESPACE_SPLIT_REGEX);
    }

    private List<FetchNoteResponse> findAll(Pageable pageable) {
        return noteRepository.findAll(pageable)
                .map(noteMapper::toFetchResponse)
                .toList();
    }

    private List<FetchNoteResponse> findAllByTags(Set<NoteType> filters, Pageable pageable) {
        return noteRepository.findAllByTags(filters, pageable)
                .stream()
                .map(noteMapper::toFetchResponse)
                .toList();
    }
}
