package com.example.CESIZen.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class AllUserException extends Exception {
    private HttpStatus statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String path;

    public AllUserException(HttpStatus statusCode, LocalDateTime timestamp, String message, String path) {
        super(message);
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
    }
}

