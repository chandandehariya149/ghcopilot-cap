# Requirements

- Story/source: `.github/skills/agentic-sdlc/user-story-input.md` (User Story: Study Planner; supplied story and eight acceptance criteria). Additional decisions: supplied by the user on 2026-10-16.
- Status: APPROVED
- Approval record: OQ-01 through OQ-06 were approved on 2026-10-16; OQ-07 was resolved and approved on 2026-10-16. The user explicitly approved the additions recorded in this update by supplying the decisions listed below on 2026-10-16.

## Context and actors

The story is for a student who wants to create study plans, organize subjects and topics, schedule study sessions, and track completion. It states that the dashboard displays total topics, completed topics, and completion percentage. No other actor is identified. The dated user decisions below add required-field details, authentication, timezone behavior, application technology, configuration, and responsive-device coverage.

## Functional requirements
| ID | Requirement | Source | Priority |
| --- | --- | --- | --- |
| FR-01 | A student can create, edit, and delete study plans. Each study plan requires a title and target completion date. | Story AC 1; user decision 2026-10-16 | Not specified |
| FR-02 | A student can add subjects and topics to a study plan. Each subject requires a name and each topic requires a title. | Story AC 2; user decision 2026-10-16 | Not specified |
| FR-03 | A student can schedule study sessions with a date, start time, and duration greater than 0 minutes. Session date and time are interpreted in that student's IANA timezone stored in their profile; past sessions are allowed. Nonexistent daylight-saving local times are rejected, and ambiguous local times require an explicit UTC offset. | Story AC 3, OQ-02; user decision 2026-10-16 | Not specified |
| FR-04 | A student can set a topic's status to Not Started, In Progress, or Completed. | Story AC 4 | Not specified |
| FR-05 | The dashboard displays total and completed topic counts across all of the student's study plans and a completion percentage rounded to the nearest whole number; it displays 0% when there are no topics. | Story AC 5, OQ-03 | Not specified |
| FR-06 | The system rejects missing required fields and invalid values. Session duration must be numeric and greater than 0 minutes; missing, non-numeric, zero, and negative durations are rejected. | Story AC 6, OQ-01; user decision 2026-10-16 | Not specified |
| FR-07 | The system authenticates students using OIDC, identifies each student by the authenticated user's ID, and allows access to and modification of only that student's plans; unauthorized responses do not reveal whether another student's plan exists. The OIDC provider is selected during setup. | Story AC 7, OQ-04; user decision 2026-10-16 | Not specified |
| FR-08 | The system returns the same not-found response for missing and foreign-owned resources, without revealing whether another student's resource exists or exposing private data. | Story AC 8, OQ-05, OQ-07 | Not specified |

## Non-functional requirements

| ID | Requirement | Measure/acceptance threshold | Source |
| --- | --- | --- | --- |
| NFR-01 | The user interface is responsive and supports desktop, tablet, and mobile devices. | Must support all three device categories. | OQ-06; user decision 2026-10-16 |
| NFR-02 | Use standard security and error-handling practices. | No numerical targets specified. | OQ-06 |

No specific performance or availability targets were supplied.

## Acceptance criteria
| ID | Given/When/Then | Requirement IDs |
| --- | --- | --- |
| AC-01 | Given a student, when they create or edit a study plan without a title or target completion date, then the request is rejected; when both required values are valid, create, edit, and delete operations are available. | FR-01, FR-06 |
| AC-02 | Given a study plan, when a student adds a subject without a name or a topic without a title, then the request is rejected; when required values are valid, the subject and topic are added to that plan. | FR-02, FR-06 |
| AC-03 | Given a study plan, when a student schedules a session, then a date, start time, and duration greater than 0 minutes are required; date and time use the student's profile IANA timezone; past sessions are accepted; nonexistent daylight-saving local times are rejected; and ambiguous local times require an explicit UTC offset. | FR-03, FR-06 |
| AC-04 | Given a topic, when a student changes its status, then the status can be Not Started, In Progress, or Completed. | FR-04 |
| AC-05 | Given the dashboard, when it is displayed, then it shows total and completed topic counts across all of the student's plans, rounds completion percentage to the nearest whole number, and shows 0% when there are no topics. | FR-05 |
| AC-06 | Given an operation with required fields, when any required field is missing or a value is invalid, then the request is rejected. In particular, session duration missing, non-numeric, zero, or negative is rejected. | FR-01, FR-02, FR-03, FR-06 |
| AC-07 | Given an unauthenticated request or an authenticated user, when they access or modify a plan, then authentication is required and only plans associated with the authenticated user's ID are accessible or modifiable; denial does not reveal whether another user's plan exists. | FR-07 |
| AC-08 | Given a requested resource, when it is missing or belongs to another student, then the system returns the same 404 Not Found response and does not reveal whether the resource exists or expose private data. | FR-08 |
| AC-09 | Given a student using the application on desktop, tablet, or mobile, when they use the interface, then the responsive UI supports that device category. | NFR-01 |

## Constraints and dependencies

- The source identifies the actor as a student and names the three topic statuses in AC 4.
- The application is a single Java web application using Spring Boot, Spring MVC, and Thymeleaf.
- For this implementation, use in-memory storage only; no database server or deployment is required. Data is volatile and resets when the application stops. This user decision supersedes the earlier PostgreSQL proposal for the current implementation.
- The deployment target is deliberately deferred until the deployment environment is known; this is not a blocking product requirement.
- The OIDC provider is to be selected during setup.
- Each student's IANA timezone is stored in their profile and used for session date/time interpretation.

## Assumptions

No additional assumptions are recorded beyond the explicit decisions below. In-memory-only local storage is an explicit user decision for this implementation; deployment remains out of scope.

## Resolved decisions

- OQ-01: Reject all missing required fields and invalid values. A study plan requires a title and target completion date; a subject requires a name; a topic requires a title; and a study session requires a date, start time, and duration. Session duration must be numeric and greater than 0 minutes; missing, non-numeric, zero, and negative values are rejected. No upper duration limit was specified. User decision supplied 2026-10-16.
- OQ-02: Session dates and times use the student's configured timezone. Past sessions are allowed for historical activity. User decision supplied 2026-10-16: store each student's IANA timezone in their profile, reject nonexistent daylight-saving local times, and require an explicit UTC offset for ambiguous local times.
- OQ-03: Dashboard totals cover all of the student's plans. Completion percentage is rounded to the nearest whole number and is 0% when there are no topics.
- OQ-04: Authenticate using OIDC and identify the student by authenticated user ID. Select the OIDC provider during setup. Deny unauthorized access without revealing whether another student's plan exists. User decision supplied 2026-10-16.
- OQ-05: The original decision requested not-found for missing resources and forbidden for existing inaccessible resources. This response distinction was superseded by the privacy-preserving OQ-07 decision below.
- OQ-06: Apply standard security and error-handling practices. The responsive UI must support desktop, tablet, and mobile. No specific performance or availability targets are defined. User decision supplied 2026-10-16.
- OQ-07 (FR-07/AC-07, FR-08/AC-08): Resolved. Return the same 404 Not Found response for both a missing resource and a resource owned by another student, preserving the requirement not to reveal existence.
- OQ-08: Use Spring Boot, Spring MVC, and Thymeleaf as a single Java web application. User decision supplied 2026-10-16.
- OQ-09: The earlier PostgreSQL and environment-based database configuration proposal is superseded for this implementation by the user's local-only direction; no database service or database credentials are required.
- OQ-10: Defer selection of the deployment target until the environment is known. This is deliberately deferred and is not a blocking product requirements question. User decision supplied 2026-10-16.
- OQ-11: Resolved by the user on 2026-10-16: use in-memory storage only and keep the application local; data resets when the application stops. No database or deployment setup is required for this implementation.
- OQ-12: Resolved by the user on 2026-10-16: for this local-only implementation, authenticate with a dev-only local form login backed by two in-memory test users (no OIDC provider or external credentials required). OIDC remains the intended production authentication (FR-07) and is deferred until a provider is selected. Owner-scoping uses the authenticated user's ID exactly as it would under OIDC.

## Open questions

No blocking requirements questions remain. Deployment target selection is deliberately deferred until the environment is known. No upper duration limit or other date-range limits were specified; do not infer such limits.

## Out of scope

The source does not state any out-of-scope capabilities. No exclusions are inferred.

## Approval
- Decision: Approved, including OQ-07, the supplied requirements decisions, and OQ-11 local in-memory storage.
- User wording: "dont enforce db or deployment , keep everything local, and start implementing bro" and selected "In memory only" when asked to choose storage behavior.
- Date: 2026-10-16