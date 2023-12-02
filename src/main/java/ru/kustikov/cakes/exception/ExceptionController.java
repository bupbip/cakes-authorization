package ru.kustikov.cakes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;


/**
 * Контроллер для обработки исключений
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({UserExistException.class, UserNotFoundException.class})
    public ResponseEntity<String> handleUserExistException(Exception ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }
}