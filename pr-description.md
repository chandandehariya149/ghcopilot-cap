## Summary
Adds a Java 17 and Spring Boot 4.1.2 todo application with a static browser UI and REST API backed by one process-local shared list. Users can add, list, complete, and delete tasks; task text is trimmed, case-insensitive duplicates are rejected, and task counts and user-visible errors are provided.

## Changes Made
- `.gitignore`: ignores generated Maven `target/` output.
- `pom.xml`: configures the Maven build for Spring Boot 4.1.2, Java 17, web MVC, and test dependencies.
- `src/main/java/com/todoapp/TodoApplication.java`: defines the Spring Boot application entry point.
- `src/main/java/com/todoapp/Todo.java`: models task identity, text, and completion state.
- `src/main/java/com/todoapp/TodoRequest.java`: defines the API request data shape.
- `src/main/java/com/todoapp/TodoService.java`: implements synchronized in-memory list operations, trimmed text, locale-independent duplicate checks, reversible completion, and missing-task handling.
- `src/main/java/com/todoapp/TodoController.java`: exposes the GET, POST, PUT, and DELETE todo endpoints.
- `src/main/java/com/todoapp/TodoError.java`: defines the stable JSON error response shape.
- `src/main/java/com/todoapp/TodoException.java`: carries the expected error code, message, and HTTP status.
- `src/main/java/com/todoapp/TodoExceptionHandler.java`: maps application exceptions to consistent API responses.
- `src/main/resources/static/index.html`: implements the responsive static UI, same-origin API calls, task counts, empty state, and text-only rendering of task/error content without browser task persistence.
- `src/test/java/com/todoapp/TodoServiceTest.java` and `TodoControllerTest.java`: cover service behavior and MVC/API status and response contracts.

## Test Evidence
- `mvn test` (recorded in `verification-report.md`): **PASS**, `BUILD SUCCESS`; 10 tests, 0 failures, 0 errors, 0 skipped (5 controller tests and 5 service tests).
- `mvn package` (recorded in `verification-report.md`): **PASS**, `BUILD SUCCESS`; the same 10 tests passed and Maven packaged `target/todo-app-0.0.1-SNAPSHOT.jar`.
- Mocked-browser mutation-order regression (recorded under CR-01 in `code-review.md`): **PASS**. With delayed mocked GET/PUT responses, stale GET data was ignored, controls were disabled during mutation, only one PUT was sent for the attempted toggles, and the final UI matched the mocked server state. This is mocked-API evidence, not a live-browser/API integration test.
- Java startup and live HTTP in final verification: **BLOCKED / UNVERIFIED**. `verification-report.md` records that the Java debug launch timed out and no server was confirmed running; no live root/API requests were made in that verification. `implementation-report.md` records an earlier successful startup and HTTP smoke, but those historical checks were not repeated as final verification.
- Actual application desktop/mobile viewport and horizontal-overflow checks: **BLOCKED / UNVERIFIED**; no real application viewport was inspected.
- Dependency vulnerability scan: **NOT RUN**. No CI workflow was found, so no CI result is available.

## Known Limitations
- The application uses one unauthenticated, process-local list shared by all clients; data resets when the server restarts. Login, durable storage, priorities, due dates, and multiple lists are out of scope.
- Final verification does not establish application startup, live HTTP integration, or desktop/mobile viewport usability. These remain release follow-up checks; source inspection and the mocked-browser regression do not replace them.
- No dependency vulnerability scan was run, and dependency vulnerability status is unknown. No CI workflow was found.
- No `CHANGELOG.md` or established changelog convention was found; no changelog entry was prepared.
- The selected target base remains `main`. Local `main` exists, but `origin/main` was not present when inspected; confirm the remote base ref before any PR creation.
- The current `feature/todo-app` worktree is dirty: it includes staged SDLC workflow/README changes, unstaged edits to `design-review.md` and `impl-plan.md`, and untracked application and SDLC artifacts. Review intended PR scope and stage boundaries before any publication; existing changes and the retained stash were preserved.
- This file is a local draft only. No commit, push, or remote PR has been made or authorized.

## Reviewer Checklist
- [ ] Requirements and acceptance criteria are satisfied, including startup and browser verification gaps.
- [ ] Security, validation, and error handling were reviewed.
- [ ] Unit and integration test evidence was reviewed, including the limits of mocked-browser evidence.
- [ ] Documentation and changelog were checked; no changelog convention was found.
- [ ] Known limitations and follow-up work are understood.
