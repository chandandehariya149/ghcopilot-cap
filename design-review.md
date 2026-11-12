# Design Review

- Status: COMPLETE
- Reviewed architecture: `architecture.md` (APPROVED)
- Requirements: `requirements.md` (APPROVED by user on 2026-11-12)
- Story: Jira CB-1, Capstone board, TODO-001
- Approval record: User approved the architecture and the Spring Boot 4.x change on 2026-11-12: "use some 4.x.x version thn, and yeah till now approved".

## Findings
| ID | Severity | Evidence and affected IDs | Impact | Recommendation | Disposition |
| --- | --- | --- | --- | --- | --- |
| DR-01 | BLOCKER, RESOLVED | User approved changing the framework line from Jira's original Spring Boot 3.x to 4.x and approved the architecture on 2026-11-12. Official [Spring Boot 4.1.2 system requirements](https://docs.spring.io/spring-boot/4.1/system-requirements.html) specify Java 17+ and Maven 3.6.3+; the [project support calendar](https://spring.io/projects/spring-boot#support) lists 4.1.x OSS support through 2027-07. | Original 3.x support-lane ambiguity is removed; Java/Maven compatibility is documented. | Pin Spring Boot 4.1.2, Java 17 baseline, and Maven 3.6.3+ in the architecture, and check installed tools/current patch before implementation. | Resolved in approved architecture ADR-01; support review remains a pre-implementation check. |
| DR-02 | MEDIUM, RESOLVED | Architecture interfaces and ADR-06 specify `201`, `200`, `204`, `400`, `404`, `409` and a stable `{code,message}` body. The user approved the architecture and current proposals on 2026-11-12. | The selected API behavior is now an approved architecture contract, distinct from Jira's endpoint and user-visible error requirements. | Preserve the contract as written and cover it in focused API tests. | Accepted by user as part of architecture approval on 2026-11-12. |
| DR-03 | LOW, RESOLVED | Architecture ADR-09 defines the empty state and `0 of 0 done`, including after final-task deletion. | Initial and final-task-removed UI states are specified. | Implement the same empty state whenever the server list is empty. | Addressed in ADR-09. |
| DR-04 | LOW, RESOLVED | Architecture ADR-09 calls for a representative narrow-viewport check of controls, messages, counts, and horizontal overflow. | Mobile verification is repeatable without inventing a numeric acceptance threshold. | Record the actual viewport and observations in verification evidence. | Addressed in ADR-09. |

## Requirement Coverage
| Requirement / criterion | Coverage | Review evidence or gap |
| --- | --- | --- |
| FR-01 / AC-01 | Covered | Static page is served at `/` from the required classpath static directory. |
| FR-02 / AC-02, AC-03 | Covered | POST create flow and client-side update without document reload are described. |
| FR-03 / AC-04 | Covered | Both completion values are supported; strikethrough, unknown-ID not-found, visible errors, and the approved HTTP contract are specified. |
| FR-04 / AC-05 | Covered | DELETE removes the task; unknown-ID not-found, visible errors, and the approved HTTP contract are specified. |
| FR-05 / AC-06 | Covered | Counts are derived and recomputed after successful mutations; the empty-list message and `0 of 0 done` state are specified in ADR-09. |
| FR-06 / AC-10 | Covered | GET returns the process-wide in-memory list; empty API result is `[]`. |
| FR-07 / AC-10 | Covered | Refresh reloads from the server; task persistence in `localStorage` is excluded. |
| FR-08 / AC-02, AC-07 | Covered | Server trims and rejects empty/whitespace-only text with validation feedback under the approved API error contract. |
| FR-09 | Covered | One process-wide list is shared among clients; no signup/login or per-user partition is added. |
| FR-10 / AC-11 | Covered | Trimmed text is compared case-insensitively with `Locale.ROOT`; duplicate scan and insertion are one synchronized operation. Keep this exact normalization behavior in implementation. |
| NFR-01 / AC-08 | Covered | Java 17+, Spring Boot 4.1.2, Maven 3.6.3+, static path, and startup command are recorded; check installed tools/current patch before implementation (DR-01). |
| NFR-02 / AC-09 | Covered | Responsive behavior has a repeatable qualitative narrow-viewport verification method in ADR-09; execution remains part of verification. |
| NFR-03 / AC-10 | Covered | In-memory state survives page refresh and resets on process restart. |

## Agreed Design Decisions

These are architectural choices consistent with the approved requirements, not additional user approvals:

- A singleton process-local `List<Todo>` serves all browser clients; data is volatile and resets at restart.
- Complete store operations, including duplicate scan plus insertion, are synchronized; reads return defensive snapshots. This addresses concurrent mutations within one application process.
- Task text is trimmed before storage and duplicate comparison, compared case-insensitively using `Locale.ROOT`, and the trimmed original spelling is retained for display.
- Completion is reversible. Task and API error text are rendered as text rather than interpolated HTML, providing an explicit XSS-safe rendering direction.
- The frontend uses same-origin REST requests and updates the page without a full reload; counts are derived from list state rather than independently mutated counters.
- No database, login, browser task persistence, or multi-instance consistency is claimed.

## Residual Risks and Open Questions

- Spring Boot 4.1.x OSS support is listed through 2027-07; check the current compatible patch before implementation.
- The shared unauthenticated list is readable and mutable by any client that can reach the server; this is an accepted consequence of FR-09 and the stated localhost scope, not a new requirement for authentication.
- State is process-local, is lost at restart, and is not shared across separate instances, as required by FR-06/07 and NFR-03.
- Mobile usability has no numeric viewport threshold; use the approved qualitative verification method in ADR-09.

## Approval
- Decision: COMPLETE; architecture approved by user
- User wording: "use some 4.x.x version thn, and yeah till now approved"
- Date: 2026-11-12

Architecture approval was given by the user on 2026-11-12. Implementation planning may proceed; implementation remains gated on explicit plan approval and repository preflight.
