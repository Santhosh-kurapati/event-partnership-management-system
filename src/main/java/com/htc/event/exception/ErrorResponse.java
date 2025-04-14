package com.htc.event.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
	 
    private String message;
    private String details;
    private LocalDateTime timestamp = LocalDateTime.now();
    
 }

