package com.example.cordo;

import com.example.cordo.entity.dto.ExceptionDTO;
import com.example.cordo.exception.BalanceLimitExceededException;
import com.example.cordo.exception.PlanNotFoundException;
import com.example.cordo.exception.ResourceNotFoundException;
import com.example.cordo.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Если в любом сервисе вылетит ResourceNotFoundException — клиент автоматически получит 404 Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
       // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); //чертов интернет я в шоке с него
        ExceptionDTO dto = new ExceptionDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(dto,HttpStatus.NOT_FOUND);
    }

    // Если баланс превышен — клиент получит 400 Bad Request
    @ExceptionHandler(BalanceLimitExceededException.class)
    public ResponseEntity<@NonNull ExceptionDTO> handleBadRequest(BalanceLimitExceededException ex, HttpServletRequest request) {
        ExceptionDTO dto = new ExceptionDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(dto,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionDTO> nullPointerException(NullPointerException ex, HttpServletRequest request) { //Бэкенд место жесткое если на время MVP прикольные названия были актуальны то на полировке придется сносить это явный минус и плюс ведь код это исскуство и здесь оно выражено по другому
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Internal_Server_Error");
        ExceptionDTO dto = new ExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(dto,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> userNotFound(UserNotFoundException ex, HttpServletRequest request) {
//        return ResponseEntity.notFound().build();
        ExceptionDTO dto = new ExceptionDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(dto,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicate(DataIntegrityViolationException ex, HttpServletRequest request) {
//        return ResponseEntity.status(HttpStatus.CONFLICT)
//                .body(Map.of("error", "Data integrity violation"));
        ExceptionDTO dto = new ExceptionDTO(HttpStatus.CONFLICT.value(), "Пользователь с таким email уже существует", request.getRequestURI());
        return new ResponseEntity<>(dto,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<java.util.Map<String, String>> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex
    ) {
        java.util.Map<String, String> errors = new java.util.HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Отдаем красивый JSON с ошибками полей и статусом 400 Bad Request
        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDTO> handleAuth(AuthenticationException e, HttpServletRequest request) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body(Map.of("error", "Invalid credentials"));
        ExceptionDTO dto = new ExceptionDTO(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(dto,HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ExceptionDTO> handlePlanNotFound(PlanNotFoundException ex, HttpServletRequest request) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(Map.of("error", ex.getMessage() != null ? ex.getMessage() : "Plan not found"));
        ExceptionDTO dto = new ExceptionDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(dto,HttpStatus.NOT_FOUND);
    }
}

