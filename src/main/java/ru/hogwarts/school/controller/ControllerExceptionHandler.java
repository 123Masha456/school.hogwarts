package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hogwarts.school.exceptions.FacultyException;
import ru.hogwarts.school.exceptions.StudentException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(StudentException.class)
    public ResponseEntity<String> handleStudentException(StudentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(FacultyException.class)
    public ResponseEntity<String> handlerFacultyException(FacultyException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
}
