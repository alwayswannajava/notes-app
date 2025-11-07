package com.notesapp.dto.request;

import com.notesapp.domain.NoteType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.Set;

public record CreateNoteRequest(
        @NotBlank(message = "title can't be blank")
        @Size(min = 2, max = 255)
        String title,

        @FutureOrPresent
        @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
        LocalDateTime createdDate,

        @NotBlank
        @Size(min = 2, max = 255)
        String text,

        Set<NoteType> tags
) {
}
