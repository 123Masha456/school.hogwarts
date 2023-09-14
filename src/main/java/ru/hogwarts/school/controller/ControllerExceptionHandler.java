package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hogwarts.school.exceptions.FacultyException;
import ru.hogwarts.school.exceptions.StudentException;
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler({StudentException.class, FacultyException.class})
    public ResponseEntity<String> handleStudentException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
