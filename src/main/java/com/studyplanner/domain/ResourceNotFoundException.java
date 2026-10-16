package com.studyplanner.domain;

/**
 * Thrown for both a missing resource and a resource owned by another student,
 * so callers cannot distinguish the two cases (FR-08 / ADR-01).
 */
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException() {
		super("Not found");
	}
}
