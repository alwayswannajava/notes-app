package com.notesapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notesapp.dto.request.CreateNoteRequest;
import com.notesapp.dto.request.UpdateNoteRequest;
import com.notesapp.dto.response.CreateNoteResponse;
import com.notesapp.dto.response.FetchNoteResponse;
import com.notesapp.dto.response.UpdateNoteResponse;
import com.notesapp.service.NoteService;
import com.notesapp.domain.NoteType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NoteController noteController;

    private final String BASE_URL = "/api/v1/notes";
    private final String NOTE_ID = "testNoteId123";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noteController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void createNote_ShouldReturnCreatedAndResponse() throws Exception {
        CreateNoteRequest request = new CreateNoteRequest("Test Title", "Test Content", Set.of(NoteType.PERSONAL));
        CreateNoteResponse expectedResponse = new CreateNoteResponse(NOTE_ID, LocalDateTime.now());

        when(noteService.createNote(any(CreateNoteRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post(BASE_URL + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(noteService, times(1)).createNote(any(CreateNoteRequest.class));
    }

    @Test
    void updateNote_ShouldReturnOkAndResponse() throws Exception {
        UpdateNoteRequest request = new UpdateNoteRequest("Updated Title", "Updated Content", Set.of(NoteType.BUSINESS));
        UpdateNoteResponse expectedResponse = new UpdateNoteResponse(NOTE_ID, "Updated Title", Set.of(NoteType.BUSINESS));

        when(noteService.updateNote(eq(NOTE_ID), any(UpdateNoteRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(patch(BASE_URL + "/{noteId}", NOTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(noteService, times(1)).updateNote(eq(NOTE_ID), any(UpdateNoteRequest.class));
    }

    @Test
    void deleteNote_ShouldReturnNoContent() throws Exception {
        doNothing().when(noteService).deleteNote(NOTE_ID);

        mockMvc.perform(delete(BASE_URL + "/{noteId}", NOTE_ID))
                .andExpect(status().isNoContent());

        verify(noteService, times(1)).deleteNote(eq(NOTE_ID));
    }

    @Test
    void fetchUniqueWords_ShouldReturnOkAndMap() throws Exception {
        Map<String, Long> expectedMap = Map.of("word", 2L, "test", 1L);

        when(noteService.fetchUniqueWords(eq(NOTE_ID))).thenReturn(expectedMap);

        mockMvc.perform(get(BASE_URL + "/{noteId}/stats", NOTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.word", is(2)))
                .andExpect(jsonPath("$.test", is(1)));

        verify(noteService, times(1)).fetchUniqueWords(eq(NOTE_ID));
    }

    @Test
    void fetchText_ShouldReturnOkAndString() throws Exception {
        String expectedText = "It's a sample note text.";

        when(noteService.fetchTextById(eq(NOTE_ID))).thenReturn(expectedText);

        mockMvc.perform(get(BASE_URL + "/text/{noteId}", NOTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(is(expectedText)));

        verify(noteService, times(1)).fetchTextById(eq(NOTE_ID));
    }

    @Test
    void fetchAll_ShouldReturnOkAndList_WithFiltersAndPaging() throws Exception {
        FetchNoteResponse note1 = new FetchNoteResponse("id1", LocalDateTime.now());
        List<FetchNoteResponse> expectedList = List.of(note1);

        Set<NoteType> filters = Set.of(NoteType.BUSINESS);
        Pageable expectedPageable = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdDate"));

        when(noteService.fetchAllNotes(eq(filters), any(Pageable.class))).thenReturn(expectedList);

        mockMvc.perform(get(BASE_URL + "/all")
                        .param("filters", "BUSINESS"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(expectedList.size())));

        verify(noteService, times(1)).fetchAllNotes(eq(filters), any(Pageable.class));
    }
}
