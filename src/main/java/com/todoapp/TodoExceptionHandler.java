package com.todoapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TodoExceptionHandler {

	@ExceptionHandler(TodoException.class)
	public ResponseEntity<TodoError> handleTodoException(TodoException exception) {
		return ResponseEntity.status(exception.status())
				.body(new TodoError(exception.code(), exception.getMessage()));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<TodoError> handleMalformedRequest() {
		return ResponseEntity.badRequest()
				.body(new TodoError("MALFORMED_REQUEST", "Request body is missing or malformed."));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<TodoError> handleUnexpectedFailure() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new TodoError("INTERNAL_ERROR", "An unexpected error occurred."));
	}
}