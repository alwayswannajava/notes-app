package com.notesapp.dto.response;

import com.notesapp.domain.NoteType;
import java.util.Set;

public record UpdateNoteResponse(
        String title,
        String text,
        Set<NoteType> tags
) {
}
