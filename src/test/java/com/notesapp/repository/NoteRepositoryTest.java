package com.notesapp.repository;

import com.notesapp.domain.Note;
import com.notesapp.domain.NoteType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("NoteRepository Integration Tests")
public class NoteRepositoryTest extends AbstractBaseIntegrationTest {

    @Autowired
    private NoteRepository noteRepository;

    @Test
    @DisplayName("Test save() method")
    public void givenNoteRepository_whenSaveAndRetrieveNote_thenOk() {
        var note = new Note("hgaguwqeqw9",
                "Save Note", LocalDateTime.now(), "This is a save note", Set.of("PERSONAL"));

        var savedNote = noteRepository.save(note);
        Optional<Note> optionalNote = noteRepository.findById(savedNote.getId());

        var retrievedNote = optionalNote.get();

        assertThat(savedNote).isNotNull();
        assertThat(savedNote.getId()).isEqualTo(retrievedNote.getId());
        assertThat(optionalNote.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Test findById() method")
    public void givenNoteRepository_whenFindById_thenOk() {
        var note = new Note("note123",
                "Sample Note", LocalDateTime.now(), "This is a sample note", Set.of("IMPORTANT"));
        noteRepository.save(note);
        Optional<Note> optionalNote = noteRepository.findById("note123");

        assertThat(optionalNote.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Test deleteById() method")
    public void givenNoteRepository_whenDeleteById_thenOk() {
        var note = new Note("noteToDelete",
                "Note to Delete", LocalDateTime.now(), "This note will be deleted", Set.of("BUSINESS"));
        noteRepository.save(note);
        noteRepository.deleteById("noteToDelete");
        Optional<Note> optionalNote = noteRepository.findById("noteToDelete");

        assertThat(optionalNote.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Test findAllByTags() method")
    public void givenNoteRepository_whenFindAllByTags_thenOk() {
        var note1 = new Note("note1",
                "Note 1", LocalDateTime.now(), "This is note 1", Set.of("PERSONAL"));
        var note2 = new Note("note2",
                "Note 2", LocalDateTime.now(), "This is note 2", Set.of("BUSINESS"));
        noteRepository.save(note1);
        noteRepository.save(note2);

        Set<NoteType> tags = new HashSet<>();
        tags.add(NoteType.PERSONAL);
        var notes = noteRepository.findAllByTags(tags, null);

        assertThat(notes).isNotNull();
        assertThat(notes.size()).isEqualTo(1);
        assertThat(notes.get(0).getId()).isEqualTo("note1");
    }

    @Test
    @DisplayName("Test findAll() with page method")
    public void givenNoteRepository_whenFindAllWithPage_thenOk() {
        for (int i = 1; i <= 15; i++) {
            var note = new Note("note" + i,
                    "Note " + i, LocalDateTime.now(), "This is note " + i, Set.of("PERSONAL"));
            noteRepository.save(note);
        }

        var notesPage = noteRepository.findAll(PageRequest.of(0, 10));

        assertThat(notesPage.getContent()).isNotNull();
        assertThat(notesPage.getContent().size()).isEqualTo(10);
    }
}

