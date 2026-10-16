package com.studyplanner.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Subject {

	private final long id;
	private final String name;
	private final List<Topic> topics = new ArrayList<>();

	public Subject(long id, String name) {
		this.id = id;
		this.name = Objects.requireNonNull(name, "name");
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Topic> getTopics() {
		return Collections.unmodifiableList(topics);
	}

	void addTopic(Topic topic) {
		topics.add(topic);
	}
}
