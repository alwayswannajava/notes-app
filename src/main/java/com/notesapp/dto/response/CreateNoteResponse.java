package com.notesapp.dto.response;

import java.time.LocalDateTime;

public record CreateNoteResponse(
        String title,
        LocalDateTime createdDate
) {
}
