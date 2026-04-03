package com.example.CESIZen.controller.exception;

import com.example.CESIZen.dto.error.ErrorResponse;
import com.example.CESIZen.exception.AllUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(AllUserException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserArgument(AllUserException ex){
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getStatusCode(),
                ex.getTimestamp(),
                ex.getMessage(),
                ex.getPath()
        );

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now(),
                errors,
                "/api/v1/auth/register-admin"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
