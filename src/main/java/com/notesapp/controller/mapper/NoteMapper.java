package com.notesapp.controller.mapper;

import com.notesapp.config.MapperConfig;
import com.notesapp.domain.Note;
import com.notesapp.dto.request.CreateNoteRequest;
import com.notesapp.dto.response.CreateNoteResponse;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface NoteMapper {
    CreateNoteResponse toResponse(Note note);

    Note toNote(CreateNoteRequest request);

}
