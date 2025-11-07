package com.notesapp.dto.request;

import com.notesapp.domain.NoteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateNoteRequest(
        @NotBlank(message = "title can't be blank")
        @Size(min = 2, max = 255, message = "title length must be between 2 and 255 characters")
        String title,

        @NotBlank
        @Size(min = 2, max = 255, message = "text length must be between 2 and 255 characters")
        String text,

        Set<NoteType> tags
) {
}
