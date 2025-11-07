package com.notesapp.dto.response;

import java.time.LocalDateTime;

public record FetchNoteResponse(
        String title,
        LocalDateTime createdDate
) {
}
