package com.notesapp.controller.exception;

import com.mongodb.MongoException;
import com.notesapp.service.exception.NoteNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionTranslator {
    private static final String MEDIA_TYPE_JSON = "application/json";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest request) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation failed");
        problem.setDetail(ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ")));
        problem.setType(URI.create("invalid-input"));
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.valueOf(MEDIA_TYPE_JSON))
                .body(problem);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoteNotFoundException(NoteNotFoundException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Not not found");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("note-not-found"));
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.valueOf(MEDIA_TYPE_JSON))
                .body(problem);
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<ProblemDetail> handleMongoException(MongoException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problem.setTitle("Note processing error");
        problem.setDetail(ex.getMessage());
        problem.setType(URI.create("note-process-error"));
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.valueOf(MEDIA_TYPE_JSON))
                .body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Internal server error");
        problem.setDetail("Unexpected error occurred");
        problem.setType(URI.create("internal-error"));
        problem.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.valueOf(MEDIA_TYPE_JSON))
                .body(problem);
    }
}
