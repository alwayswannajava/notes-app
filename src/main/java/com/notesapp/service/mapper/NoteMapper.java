package com.notesapp.service.mapper;

import com.notesapp.config.MapperConfig;
import com.notesapp.domain.Note;
import com.notesapp.domain.NoteType;
import com.notesapp.dto.request.CreateNoteRequest;
import com.notesapp.dto.request.UpdateNoteRequest;
import com.notesapp.dto.response.CreateNoteResponse;
import com.notesapp.dto.response.FetchNoteResponse;
import com.notesapp.dto.response.UpdateNoteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface NoteMapper {
    CreateNoteResponse toCreateResponse(Note note);

    Note toNote(CreateNoteRequest request);

    UpdateNoteResponse toUpdateResponse(Note note);

    Note toNote(@MappingTarget Note note, UpdateNoteRequest request);

    FetchNoteResponse toFetchResponse(Note note);

    List<FetchNoteResponse> toFetchNoteResponseList(List<Note> notes);

}
