package com.studyplanner.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

public final class StudySession {

	private final long id;
	private final ZonedDateTime start;
	private final int durationMinutes;

	public StudySession(long id, ZonedDateTime start, int durationMinutes) {
		if (durationMinutes <= 0) {
			throw new IllegalArgumentException("durationMinutes must be greater than 0");
		}
		this.id = id;
		this.start = Objects.requireNonNull(start, "start");
		this.durationMinutes = durationMinutes;
	}

	public long getId() {
		return id;
	}

	public ZonedDateTime getStart() {
		return start;
	}

	public int getDurationMinutes() {
		return durationMinutes;
	}

	/**
	 * Resolves a local date/time in the student's zone. Rejects times that do not
	 * exist (DST gap) and requires an explicit offset for ambiguous times (DST overlap).
	 */
	public static ZonedDateTime resolveStart(LocalDateTime local, ZoneId zone, ZoneOffset explicitOffset) {
		var transition = zone.getRules().getTransition(local);
		if (transition != null && transition.isGap()) {
			throw new InvalidSessionTimeException("The selected time does not exist in " + zone + " due to daylight saving.");
		}
		var validOffsets = zone.getRules().getValidOffsets(local);
		if (validOffsets.size() > 1) {
			if (explicitOffset == null || !validOffsets.contains(explicitOffset)) {
				throw new InvalidSessionTimeException(
						"The selected time is ambiguous in " + zone + "; choose a UTC offset: " + validOffsets);
			}
			return ZonedDateTime.ofStrict(local, explicitOffset, zone);
		}
		return ZonedDateTime.of(local, zone);
	}
}
