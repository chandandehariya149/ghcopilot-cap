package com.studyplanner.domain;

public class InvalidSessionTimeException extends RuntimeException {

	public InvalidSessionTimeException(String message) {
		super(message);
	}
}
