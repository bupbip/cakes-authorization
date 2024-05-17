package ru.kustikov.cakes.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


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