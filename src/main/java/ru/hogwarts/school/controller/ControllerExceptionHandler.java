package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.hogwarts.school.exceptions.AvatarException;
import ru.hogwarts.school.exceptions.FacultyException;
import ru.hogwarts.school.exceptions.StudentException;

import java.util.Arrays;

@ControllerAdvice
public class ControllerExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    @ExceptionHandler({StudentException.class, FacultyException.class, AvatarException.class})
    public ResponseEntity<String> handleStudentException(RuntimeException ex) {

        logger.warn(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex){

        logger.error(String.valueOf(ex));
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Server has some problems, sorry");
    }


}
