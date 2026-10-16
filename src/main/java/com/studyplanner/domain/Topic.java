package com.studyplanner.domain;

import java.util.Objects;

public final class Topic {

	private final long id;
	private final String title;
	private TopicStatus status = TopicStatus.NOT_STARTED;

	public Topic(long id, String title) {
		this.id = id;
		this.title = Objects.requireNonNull(title, "title");
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public TopicStatus getStatus() {
		return status;
	}

	public void setStatus(TopicStatus status) {
		this.status = Objects.requireNonNull(status, "status");
	}
}
