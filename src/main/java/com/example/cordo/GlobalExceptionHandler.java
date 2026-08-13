package com.example.cordo;

import com.example.cordo.exception.BalanceLimitExceededException;
import com.example.cordo.exception.NullPointException;
import com.example.cordo.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Если в любом сервисе вылетит ResourceNotFoundException — клиент автоматически получит 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); //чертов интернет я в шоке с него
    }

    // Если баланс превышен — клиент получит 400 Bad Request
    @ExceptionHandler(BalanceLimitExceededException.class)
    public ResponseEntity<String> handleBadRequest(BalanceLimitExceededException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(NullPointException.class)
    public ResponseEntity<String> NullKapec(NullPointException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal_Server_Error");
    }
}

