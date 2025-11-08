package com.notesapp.service;

import com.notesapp.domain.Note;
import com.notesapp.domain.NoteType;
import com.notesapp.dto.request.CreateNoteRequest;
import com.notesapp.dto.request.UpdateNoteRequest;
import com.notesapp.dto.response.CreateNoteResponse;
import com.notesapp.dto.response.FetchNoteResponse;
import com.notesapp.dto.response.UpdateNoteResponse;
import com.notesapp.repository.NoteRepository;
import com.notesapp.service.exception.NoteNotFoundException;
import com.notesapp.service.impl.NoteServiceImpl;
import com.notesapp.service.mapper.NoteMapper;
import com.notesapp.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("NoteService Unit Tests")
public class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @InjectMocks
    private NoteServiceImpl noteService;

    private Note note;
    private CreateNoteRequest createNoteRequest;
    private UpdateNoteRequest updateNoteRequest;
    private CreateNoteResponse createNoteResponse;
    private UpdateNoteResponse updateNoteResponse;
    private FetchNoteResponse fetchNoteResponse;
    private Pageable pageable;
    private Page<Note> notePage;

    @BeforeEach
    void setUp() {
        note = TestUtil.createNote();
        createNoteRequest = TestUtil.createCreateNoteRequest();
        updateNoteRequest = TestUtil.createUpdateNoteRequest();
        createNoteResponse = TestUtil.createCreateNoteResponse();
        updateNoteResponse = TestUtil.createUpdateNoteResponse();
        fetchNoteResponse = TestUtil.createFetchNoteResponse();
        pageable = TestUtil.createPageable();
        notePage = TestUtil.createNotePage();
    }

    @Test
    @DisplayName("Test createNote() method")
    void createNote_ValidCreateNoteRequest_ReturnsCreateNoteResponse() {
        when(noteMapper.toNote(createNoteRequest)).thenReturn(note);
        when(noteRepository.save(note)).thenReturn(note);
        when(noteMapper.toCreateResponse(note)).thenReturn(createNoteResponse);

        CreateNoteResponse expected = createNoteResponse;
        CreateNoteResponse actual = noteService.createNote(createNoteRequest);

        assertEquals(expected, actual);
        verify(noteMapper).toNote(createNoteRequest);
        verify(noteRepository).save(note);
        verify(noteMapper).toCreateResponse(note);
        verifyNoMoreInteractions(noteRepository, noteMapper);
    }

    @Test
    @DisplayName("Test updateNote() method")
    void updateNote_ExistingNoteId_ReturnsUpdateNoteResponse() {
        String noteId = "note123";
        Note updatedNote = TestUtil.createUpdatedNote();

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));
        when(noteMapper.toNote(note, updateNoteRequest)).thenReturn(updatedNote);
        when(noteRepository.save(updatedNote)).thenReturn(updatedNote);
        when(noteMapper.toUpdateResponse(updatedNote)).thenReturn(updateNoteResponse);

        UpdateNoteResponse expected = updateNoteResponse;
        UpdateNoteResponse actual = noteService.updateNote(noteId, updateNoteRequest);

        assertEquals(expected, actual);
        verify(noteRepository).findById(noteId);
        verify(noteMapper).toNote(note, updateNoteRequest);
        verify(noteRepository).save(updatedNote);
        verify(noteMapper).toUpdateResponse(updatedNote);
        verifyNoMoreInteractions(noteRepository, noteMapper);
    }

    @Test
    @DisplayName("Test updateNote() with non-existent id")
    void updateNote_NonExistentNoteId_ThrowsNoteNotFoundException() {
        String noteId = "nonexistent";
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.updateNote(noteId, updateNoteRequest))
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessageContaining("Note with id: " + noteId + " not found");

        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
        verifyNoInteractions(noteMapper);
    }

    @Test
    @DisplayName("Test deleteNote() method")
    void deleteNote_ValidNoteId_DeletesNote() {
        String noteId = "note123";
        doNothing().when(noteRepository).deleteById(noteId);

        noteService.deleteNote(noteId);

        verify(noteRepository).deleteById(noteId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchById() method")
    void fetchById_ExistingNoteId_ReturnsFetchNoteResponse() {
        String noteId = "note123";
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));
        when(noteMapper.toFetchResponse(note)).thenReturn(fetchNoteResponse);

        FetchNoteResponse expected = fetchNoteResponse;
        FetchNoteResponse actual = noteService.fetchById(noteId);

        assertEquals(expected, actual);
        verify(noteRepository).findById(noteId);
        verify(noteMapper).toFetchResponse(note);
        verifyNoMoreInteractions(noteRepository, noteMapper);
    }

    @Test
    @DisplayName("Test fetchById() with non-existent id")
    void fetchById_NonExistentNoteId_ThrowsNoteNotFoundException() {
        String noteId = "nonexistent";
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.fetchById(noteId))
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessageContaining("Note with id: " + noteId + " not found");

        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
        verifyNoInteractions(noteMapper);
    }

    @Test
    @DisplayName("Test fetchTextById() method")
    void fetchTextById_ExistingNoteId_ReturnsText() {
        String noteId = "note123";
        String expectedText = "This is a test note note";
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(note));

        String expected = expectedText;
        String actual = noteService.fetchTextById(noteId);

        assertEquals(expected, actual);
        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchTextById() with non-existent id")
    void fetchTextById_NonExistentNoteId_ThrowsNoteNotFoundException() {
        String noteId = "nonexistent";
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.fetchTextById(noteId))
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessageContaining("Note with id: " + noteId + " not found");

        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchUniqueWords() method")
    void fetchUniqueWords_ValidNoteText_ReturnsWordFrequencyMap() {
        String noteId = "note123";
        Note noteWithText = TestUtil.createNoteWithText("note is just a note");
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(noteWithText));

        Map<String, Long> actual = noteService.fetchUniqueWords(noteId);

        assertThat(actual).containsEntry("note", 2L);
        assertThat(actual).containsEntry("is", 1L);
        assertThat(actual).containsEntry("just", 1L);
        assertThat(actual).containsEntry("a", 1L);
        assertThat(actual).hasSize(4);
        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchUniqueWords() filters non-alphabetic words")
    void fetchUniqueWords_TextWithNumbers_FiltersOutNonAlphabeticWords() {
        String noteId = "note123";
        Note noteWithNumbers = TestUtil.createNoteWithText("hello 123 world test123 hello");
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(noteWithNumbers));

        Map<String, Long> actual = noteService.fetchUniqueWords(noteId);

        assertThat(actual).containsEntry("hello", 2L);
        assertThat(actual).containsEntry("world", 1L);
        assertThat(actual).doesNotContainKey("123");
        assertThat(actual).doesNotContainKey("test123");
        assertThat(actual).hasSize(2);
        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchUniqueWords() with empty text")
    void fetchUniqueWords_EmptyText_ReturnsEmptyMap() {
        String noteId = "note123";
        Note emptyNote = TestUtil.createNoteWithText("   ");
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(emptyNote));

        Map<String, Long> actual = noteService.fetchUniqueWords(noteId);

        assertThat(actual).isEmpty();
        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchUniqueWords() with only special characters")
    void fetchUniqueWords_OnlyNumbersAndSpecialChars_ReturnsEmptyMap() {
        String noteId = "note123";
        Note specialCharsNote = TestUtil.createNoteWithText("123 456 !@# $%^");
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(specialCharsNote));

        Map<String, Long> actual = noteService.fetchUniqueWords(noteId);

        assertThat(actual).isEmpty();
        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchUniqueWords() with non-existent id")
    void fetchUniqueWords_NonExistentNoteId_ThrowsNoteNotFoundException() {
        String noteId = "nonexistent";
        when(noteRepository.findById(noteId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> noteService.fetchUniqueWords(noteId))
                .isInstanceOf(NoteNotFoundException.class)
                .hasMessageContaining("Note with id: " + noteId + " not found");

        verify(noteRepository).findById(noteId);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchAllNotes() without filters")
    void fetchAllNotes_NoFilters_ReturnsAllNotes() {
        List<FetchNoteResponse> expectedList = TestUtil.createFetchNoteResponseList();
        when(noteRepository.findAll(pageable)).thenReturn(notePage);
        when(noteMapper.toFetchResponse(any(Note.class))).thenReturn(fetchNoteResponse);

        List<FetchNoteResponse> actual = noteService.fetchAllNotes(null, pageable);

        assertThat(actual).isNotEmpty();
        verify(noteRepository).findAll(pageable);
        verify(noteRepository, never()).findAllByTags(any(), any());
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchAllNotes() with tag filters")
    void fetchAllNotes_WithTagFilters_ReturnsFilteredNotes() {
        Set<NoteType> filters = Set.of(NoteType.BUSINESS);
        List<Note> notes = TestUtil.createNoteList();
        when(noteRepository.findAllByTags(filters, pageable)).thenReturn(notes);
        when(noteMapper.toFetchResponse(any(Note.class))).thenReturn(fetchNoteResponse);

        List<FetchNoteResponse> actual = noteService.fetchAllNotes(filters, pageable);

        assertThat(actual).isNotEmpty();
        verify(noteRepository).findAllByTags(filters, pageable);
        verify(noteRepository, never()).findAll(any(Pageable.class));
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchAllNotes() with no notes found")
    void fetchAllNotes_NoNotesFound_ReturnsEmptyList() {
        Page<Note> emptyPage = TestUtil.createEmptyNotePage();
        when(noteRepository.findAll(pageable)).thenReturn(emptyPage);

        List<FetchNoteResponse> actual = noteService.fetchAllNotes(null, pageable);

        assertThat(actual).isEmpty();
        verify(noteRepository).findAll(pageable);
        verifyNoMoreInteractions(noteRepository);
    }

    @Test
    @DisplayName("Test fetchAllNotes() with pagination")
    void fetchAllNotes_MultipleNotesWithPagination_ReturnsPagedResults() {
        List<Note> notes = TestUtil.createMultipleNotes();
        Page<Note> pagedNotes = TestUtil.createPageWithNotes(notes, pageable);
        List<FetchNoteResponse> responses = TestUtil.createMultipleFetchResponses();

        when(noteRepository.findAll(pageable)).thenReturn(pagedNotes);
        when(noteMapper.toFetchResponse(notes.get(0))).thenReturn(responses.get(0));
        when(noteMapper.toFetchResponse(notes.get(1))).thenReturn(responses.get(1));

        List<FetchNoteResponse> actual = noteService.fetchAllNotes(null, pageable);

        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).title()).isEqualTo("1");
        assertThat(actual.get(1).title()).isEqualTo("2");
        verify(noteRepository).findAll(pageable);
        verifyNoMoreInteractions(noteRepository);
    }
}
