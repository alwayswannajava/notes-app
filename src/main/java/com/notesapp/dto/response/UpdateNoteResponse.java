package com.notesapp.dto.response;

import java.time.LocalDateTime;

public record UpdateNoteResponse(
        String title,
        LocalDateTime createdDate
) {
}
