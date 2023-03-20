package edu.school21.restful.exceptions.lesson;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class LessonGlobalExceptionHandler {

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<LessonError> handleUserNotFound(LessonNotFoundException e) {
        LessonError courseError = new LessonError();
        courseError.setTimestamp(LocalDateTime.now());
        courseError.setStatus(HttpStatus.NOT_FOUND.value());
        courseError.setMessage("Course is not found");
        List<String> a = new ArrayList<>();
        a.add(e.getMessage());
        courseError.setErrors(a);
        return ResponseEntity
                .status(courseError.getStatus())
                .body(courseError);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<LessonError> handleUserNotFound(InvalidFormatException e) {
        LessonError courseError = new LessonError();
        courseError.setTimestamp(LocalDateTime.now());
        courseError.setStatus(HttpStatus.NOT_FOUND.value());
        courseError.setMessage("Course is not found");
        List<String> a = new ArrayList<>();
        a.add(e.getMessage());
        courseError.setErrors(a);
        return ResponseEntity
                .status(courseError.getStatus())
                .body(courseError);
    }

    @ExceptionHandler({LessonNotSavedException.class, DateTimeParseException.class})
    public ResponseEntity<LessonError> handleCourseNotFound(LessonNotSavedException e, DateTimeParseException ef) {
        LessonError courseError = new LessonError();
        courseError.setTimestamp(LocalDateTime.now());
        courseError.setStatus(HttpStatus.BAD_REQUEST.value());
        courseError.setMessage("Course is not saved correctly");
        List<String> a = new ArrayList<>();
        System.out.println(e.getMessage() + "Hello");
        if (e != null) {
            a.add(e.getMessage());
        } else if (ef != null) {
            a.add(ef.getMessage());
        }
        courseError.setErrors(a);
        return ResponseEntity
                .status(courseError.getStatus())
                .body(courseError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<LessonError> handleException(HttpMessageNotReadableException e) {
        LessonError courseError = new LessonError();
        courseError.setTimestamp(LocalDateTime.now());
        courseError.setStatus(HttpStatus.BAD_REQUEST.value());
        courseError.setMessage("Course is not saved correctly");
        List<String> a = new ArrayList<>();
        a.add(e.getMessage());
        courseError.setErrors(a);
        return ResponseEntity
                .status(courseError.getStatus())
                .body(courseError);
    }

}
