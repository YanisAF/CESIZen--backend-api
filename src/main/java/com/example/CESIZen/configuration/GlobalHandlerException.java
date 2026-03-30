package com.example.CESIZen.configuration;

import com.example.CESIZen.dto.error.ErrorResponse;
import com.example.CESIZen.exception.AllUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
