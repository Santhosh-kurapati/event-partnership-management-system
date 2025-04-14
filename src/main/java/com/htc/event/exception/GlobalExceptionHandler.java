package com.htc.event.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EventTypeNotFoundException.class)
	public ResponseEntity<?> handleEventTypeNotFoundException(EventTypeNotFoundException ex) {
		ErrorResponse response = new ErrorResponse("Event not found", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	// String message = String.format(eventNotFoundMessage, ex.getId());

	@ExceptionHandler(IsPartnerNotFoundException.class)
	public ResponseEntity<Object> handleIsPartnerNotFoundException(IsPartnerNotFoundException ex) {
		ErrorResponse response = new ErrorResponse("partner not found", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EventNotFoundException.class)
	public ResponseEntity<?> handleEventNotFoundException(EventNotFoundException ex) {
		// Create a response with the appropriate HTTP status and message
		ErrorResponse response = new ErrorResponse("Event not found", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PartnerNotFoundException.class)
	public ResponseEntity<Object> handlePartnerNotFoundException(PartnerNotFoundException ex) {
		ErrorResponse response = new ErrorResponse("partner not found", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PartnerRoleNotFoundException.class)
	public ResponseEntity<Object> handlePartnerRoleNotFoundException(PartnerRoleNotFoundException ex) {
		ErrorResponse response = new ErrorResponse("partner role not found", ex.getMessage(), LocalDateTime.now());
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", "An error occurred");
		body.put("error", ex.getMessage());
		body.put("path", request.getDescription(false));

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
