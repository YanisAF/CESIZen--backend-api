package com.example.CESIZen.dto.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {
    private HttpStatus statusCode;
    private LocalDateTime timestamp;
    private String message;
    private String path;

    public ErrorResponse(HttpStatus statusCode, LocalDateTime timestamp, String message, String path){
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.path = path;
    }
}
