package com.studyplanner.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class StudyPlan {

	private final long id;
	private final String ownerId;
	private String title;
	private LocalDate targetCompletionDate;
	private final List<Subject> subjects = new ArrayList<>();
	private final List<StudySession> sessions = new ArrayList<>();

	public StudyPlan(long id, String ownerId, String title, LocalDate targetCompletionDate) {
		this.id = id;
		this.ownerId = Objects.requireNonNull(ownerId, "ownerId");
		this.title = Objects.requireNonNull(title, "title");
		this.targetCompletionDate = Objects.requireNonNull(targetCompletionDate, "targetCompletionDate");
	}

	public long getId() {
		return id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public String getTitle() {
		return title;
	}

	public LocalDate getTargetCompletionDate() {
		return targetCompletionDate;
	}

	public void update(String title, LocalDate targetCompletionDate) {
		this.title = Objects.requireNonNull(title, "title");
		this.targetCompletionDate = Objects.requireNonNull(targetCompletionDate, "targetCompletionDate");
	}

	public List<Subject> getSubjects() {
		return Collections.unmodifiableList(subjects);
	}

	public List<StudySession> getSessions() {
		return Collections.unmodifiableList(sessions);
	}

	void addSubject(Subject subject) {
		subjects.add(subject);
	}

	void addSession(StudySession session) {
		sessions.add(session);
	}

	public int totalTopics() {
		return subjects.stream().mapToInt(s -> s.getTopics().size()).sum();
	}

	public int completedTopics() {
		return (int) subjects.stream()
				.flatMap(s -> s.getTopics().stream())
				.filter(t -> t.getStatus() == TopicStatus.COMPLETED)
				.count();
	}
}
