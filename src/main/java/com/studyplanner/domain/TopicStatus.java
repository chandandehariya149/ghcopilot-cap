package com.studyplanner.domain;

public enum TopicStatus {
	NOT_STARTED("Not Started"),
	IN_PROGRESS("In Progress"),
	COMPLETED("Completed");

	private final String label;

	TopicStatus(String label) {
		this.label = label;
	}

	public String label() {
		return label;
	}
}
