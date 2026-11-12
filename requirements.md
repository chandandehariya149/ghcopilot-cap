# Requirements

- Story/source: Jira issue [CB-1](https://chandan-pvt.atlassian.net/browse/CB-1), summary `TO-DO APP`, project Capstone board (CB), issue type Story, status To Do. Retrieved story facts were supplied for this requirements draft.
- Status: APPROVED
- Approval record: User approved the original requirements on 2026-11-12 and explicitly approved changing the Spring Boot major line from 3.x to 4.x on 2026-11-12.

## Context and actors

- Story ID: TODO-001
- Story title: Simple Todo List with Java Backend
- User story: As a user, I want to open a web page, add tasks to a todo list, mark them as done, and delete them so I can manage tasks from any browser.
- Primary actor: A user accessing the todo page in a browser.
- The application is served at `http://localhost:8080`. Tasks are held in server memory and reset when the server restarts.

## Functional requirements
| ID | Requirement | Source | Priority |
| --- | --- | --- | --- |
| FR-01 | Serve a clean todo page at the root URL `http://localhost:8080`. | CB-1 behavior; AC-1 | Must |
| FR-02 | Allow a user to submit a task, create it through `POST /api/todos`, and show the added task without a full page reload. | CB-1 behavior and technical requirements; AC-2, AC-3 | Must |
| FR-03 | Allow a user to toggle a task between done and not done through `PUT /api/todos/{id}`; completed task text is displayed with a strikethrough. For an unknown ID, return not-found and show a user-visible error popup/message. | CB-1 behavior and technical requirements; AC-4; user decision 2026-11-12 | Must |
| FR-04 | Allow a user to delete a task through `DELETE /api/todos/{id}` and remove it from the page. For an unknown ID, return not-found and show a user-visible error popup/message. | CB-1 behavior and technical requirements; AC-5; user decision 2026-11-12 | Must |
| FR-05 | Show the total number of tasks and the number completed, in a form such as `3 of 5 done`, and update the count after add, completion, or deletion. | CB-1 behavior; AC-6 | Must |
| FR-06 | Provide `GET /api/todos` to retrieve the todo list. Tasks are held in a server-side in-memory Java `List` and reset when the server restarts. | CB-1 technical requirements and behavior | Must |
| FR-07 | Retain tasks across page refreshes by keeping them server-side; do not use browser `localStorage` as task persistence. | CB-1 behavior; AC-10 | Must |
| FR-08 | Trim leading/trailing whitespace from submitted task text; reject empty or whitespace-only input and show a validation message. | CB-1 behavior; AC-7; user decisions 2026-11-12 | Must |
| FR-09 | All browser clients use the same server-side todo list; no signup or login is required. | CB-1 user story and out-of-scope; user decision 2026-11-12 | Must |
| FR-10 | Trim leading/trailing whitespace and reject duplicate task text case-insensitively. | User decisions 2026-11-12 | Must |

## Non-functional requirements
| ID | Requirement | Measure/acceptance threshold | Source |
| --- | --- | --- | --- |
| NFR-01 | Use Java 17 or later with Spring Boot 4.1.2, Maven (`pom.xml`), plain HTML/CSS/vanilla JavaScript, and a static frontend at `src/main/resources/static/index.html`. The 4.1.2 pin is the user-authorized update to Jira's original 3.x major-line constraint. | The specified technology and file locations are used; `mvn spring-boot:run` starts without errors (AC-8). | CB-1 technical requirements, updated by user decision 2026-11-12; AC-8 |
| NFR-02 | The todo page is clean and mobile-friendly. | Page is usable on mobile; no further viewport or usability threshold is specified in CB-1. | CB-1 behavior; AC-1, AC-9 |
| NFR-03 | Keep task data in server memory rather than persistent storage. | Data resets on server restart and survives browser page refresh while the server remains running. | CB-1 behavior; AC-10 |

## Acceptance criteria
| ID | Given/When/Then | Requirement IDs |
| --- | --- | --- |
| AC-01 | Given the application is running, when the user opens the root URL, then the todo page is shown. | FR-01 |
| AC-02 | Given the todo page is open and a valid task is entered, when the user submits it, then the client sends a `POST` request and the task is added. | FR-02, FR-08 |
| AC-03 | Given a task is submitted successfully, then it appears without a full page reload. | FR-02 |
| AC-04 | Given a task is listed, when the user checks or unchecks its Done control, then the client sends a `PUT` request and the task completion state and strikethrough update accordingly; when its ID is unknown, the API returns not-found and the user sees an error popup/message. | FR-03 |
| AC-05 | Given a task is listed, when the user deletes it, then the client sends a `DELETE` request and the task is removed from the page; when the ID is unknown, the API returns not-found and the user sees an error popup/message. | FR-04 |
| AC-06 | Given the list has tasks, when a task is added, completed, or deleted, then the total and completed count updates. | FR-05 |
| AC-07 | Given the task input is empty or contains only whitespace, when the user submits it, then the task is rejected and a validation message is shown; surrounding whitespace on valid task text is trimmed before storage. | FR-08 |
| AC-08 | Given the project is set up, when `mvn spring-boot:run` is run, then the application starts without errors. | NFR-01 |
| AC-09 | Given the application is viewed on desktop or mobile, then the page is clean and mobile-friendly. | NFR-02 |
| AC-10 | Given tasks have been added, when the page is refreshed while the server is running, then the tasks remain available from the server and are not persisted using `localStorage`. | FR-06, FR-07, NFR-03 |
| AC-11 | Given a task already exists, when a user submits task text that matches it after trimming surrounding whitespace and ignoring letter case, then the duplicate is rejected and the user sees a validation message. | FR-10 |

## Constraints and dependencies

- The application target is Java 17+ and Spring Boot 4.1.2, with Maven. This supersedes Jira CB-1's original Spring Boot 3.x constraint by explicit user decision on 2026-11-12.
- The frontend uses plain HTML, CSS, and vanilla JavaScript; the static entry page is `src/main/resources/static/index.html`.
- Required REST endpoints: `GET /api/todos`, `POST /api/todos`, `PUT /api/todos/{id}`, and `DELETE /api/todos/{id}`.
- Todo data is held in an in-memory server-side Java `List`; it resets when the server restarts. All browser clients share the list and no signup/login is required.
- Page URL: `http://localhost:8080`.

## Assumptions

- No additional assumptions recorded.

## Open questions

| ID | Question | Why it matters |
| --- | --- | --- |
| Q-01 | Should whitespace-only input be rejected? | Resolved: yes; treat it as empty. |
| Q-02 | Can a completed task be unchecked? | Resolved: yes; completion toggles both directions. |
| Q-03 | With no signup, what is the list's sharing scope? | Resolved: all browser clients share one server-side list. |
| Q-04 | Are duplicate task texts allowed? | Resolved: no duplicates; comparison is case-insensitive. User clarified case-insensitive matching on 2026-11-12. |
| Q-05 | What should PUT/DELETE do for an unknown ID? | Resolved: both return not-found and show an error popup/message. |
| Q-06 | Should leading/trailing whitespace be trimmed before storing task text and checking duplicates? | Resolved: yes; trim before storage and duplicate comparison. |

## Out of scope

- Login or authentication.
- Persistent database storage.
- Due dates or priorities.
- Multiple lists or categories.

## Resolved decisions

- Q-01: Reject whitespace-only input as empty. User decision supplied 2026-11-12.
- Q-02: The Done control is reversible; users can uncheck a completed task. User decision supplied 2026-11-12.
- Q-03: No signup is required; all browser clients share the same in-memory server list. User decision supplied 2026-11-12.
- Q-04: Duplicate tasks are not allowed; trim surrounding whitespace and compare case-insensitively. User decisions supplied 2026-11-12.
- Q-05: Unknown IDs for both PUT and DELETE return not-found; show an error popup/message in the UI. User decision supplied 2026-11-12.
- NFR-01 technology update: use Spring Boot 4.x rather than the Jira story's 3.x line. User wording: "use some 4.x.x version thn, and yeah till now approved" (2026-11-12); the architecture pins the compatible current stable 4.1.2 release based on official Spring documentation.

## Approval
- Decision: APPROVED
- User wording: "approved"; subsequent technology update approved with: "use some 4.x.x version thn, and yeah till now approved"
- Date: 2026-11-12
