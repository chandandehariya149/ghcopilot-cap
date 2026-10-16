# Design Review

- Status: NEEDS DECISION
- Reviewed architecture: `architecture.md` (records user approval dated 2026-10-16, including OQ-11)
- Requirements: `requirements.md` (APPROVED 2026-10-16)
- Review date: 2026-10-16
- Approval record: The architecture's user approval is recorded in `architecture.md`. This review neither grants nor withdraws that approval.

## Findings (Original IDs Retained)
| ID | Severity | Evidence and affected IDs | Impact | Recommendation | Disposition |
| --- | --- | --- | --- | --- | --- |
| DR-01 | BLOCKER (historical) | The earlier review documented a 403/404 distinction. Current ADR-01, request flows, interfaces, and security rules require 401 for unauthenticated requests, identical 404 for missing and foreign-owned resources, and no 403 for foreign-owned resources (FR-07/FR-08; AC-07/AC-08). | The former response-policy conflict is removed. | Preserve the current response contract in implementation. | RESOLVED; current architecture is consistent with approved OQ-07. |
| DR-02 | MEDIUM (historical) | The earlier review noted malformed interface Markdown and duplicate decision registers. Current architecture has a coherent capability table and one ADR-01..05 register. | The earlier structure ambiguity is removed. | Retain these as the respective sources of truth. | RESOLVED in the current architecture. |
| DR-03 | MEDIUM (historical) | Earlier review treated identity and timezone behavior as open. Requirements and ADR-03 specify OIDC, provider selection during setup, authenticated user ID, profile-stored IANA timezone, DST-gap rejection, explicit offset for overlaps, and acceptance of past sessions (FR-03/FR-07; AC-03/AC-07). | Required behavior is specified; provider setup and the credential question in DR-10 remain outstanding. | Bind ownership only to trusted authenticated identity; preserve the timezone and DST rules. | RESOLVED for behavioral coverage. Provider choice is a setup activity; see DR-10 for credential scope. |
| DR-04 | MEDIUM (historical) | Current requirements and architecture enumerate plan title and target date, subject name, topic title, and session date, start time, and positive numeric duration; missing/invalid values are rejected (FR-01..FR-03, FR-06; AC-01..AC-03, AC-06). | The required-value behavior is implementable and testable. | Preserve these fields and do not invent duration or date limits. | RESOLVED by current requirements and ADR-02. |
| DR-05 | MEDIUM (historical) | Architecture requires owner-scoping for every plan/child read, list, aggregate, and mutation; identity comes from the trusted security context; child-parent ownership is checked in the same storage operation (FR-05, FR-07/FR-08; AC-05, AC-07/AC-08). | The authorization invariant is explicit. One child-create test omission remains DR-08. | Preserve the owner-scoping invariant and cover all paths in verification. | RESOLVED as an architecture-contract gap; test completeness remains open under DR-08. |
| DR-06 | MEDIUM (historical) | Earlier review described PostgreSQL and environment-based database credentials. Current requirements OQ-11 and ADR-04 instead specify in-memory-only local storage, no database server or DB connection credentials, volatile data lost on stop, and no deployment requirement; the deployment target is deferred. | The prior persistence and hosting ambiguity is removed. OIDC provider credentials/configuration are distinct and unresolved under DR-10. | Keep PostgreSQL explicitly superseded and deployment deferred for this implementation. | RESOLVED for storage and deployment scope. |
| DR-07 | MEDIUM (historical) | Requirements and ADR-05 specify one Java web application using Spring Boot, Spring MVC, and Thymeleaf, responsive on desktop, tablet, and mobile (NFR-01; AC-09). | The application shape and device coverage are specified; implementation behavior still needs verification. | Preserve the selected stack and verify all three device categories during implementation. | RESOLVED at architecture level. |
| DR-08 | LOW | Testability covers foreign-owned plan and child IDs for direct read, list, aggregate, update, and delete, but does not explicitly name creating a child under a foreign-owned plan. The general owner-scope/parent-ownership invariant does cover it (FR-07/FR-08; AC-07/AC-08). | The child-create path could be omitted from security verification despite the correct design invariant. | Add a test for child creation under a foreign-owned plan; assert the same 404 policy and no persisted child. | OPEN, non-blocking; carry into implementation planning and test design. |
| DR-09 | LOW | The current implementation is explicitly in-memory with no database or deployment, so there is no current schema-migration or deployment rollback mechanism to specify (ADR-04; OQ-11). The deployment section defers environment-specific operations. | Migration/versioning and recovery details matter only if a future durable-storage/deployment scope is approved. | Keep migration, rollback, and restore decisions deferred; revisit them only if that future scope is selected. | OPEN, non-blocking and deferred; not a gap in the approved local in-memory scope. |
| DR-10 | BLOCKER (historical) | OIDC is required (FR-07; OQ-04), the provider is selected during setup, and architecture configuration/security/deployment sections say to configure the provider and protect OIDC credentials. The architecture explicitly rules out database credentials but did not establish that zero external OIDC credentials or provider access is required. | Resolved by user decision OQ-12 (2026-10-16): this local-only implementation uses a dev-only form login with two in-memory test users; OIDC is deferred until a provider is selected. Identity still comes only from the Spring Security context, so owner-scoping is unchanged. | Implement the dev login as an explicit local profile; keep the security filter chain OIDC-ready; never accept client-supplied identity. | RESOLVED by OQ-12. |
| DR-11 | MEDIUM | Architecture status and Approval record say APPROVED by the user, but the Java version, Spring Boot version, Maven Wrapper, and modular boundaries are also labeled recommendations awaiting architecture approval (Technology choices; Risks, assumptions, and open questions). | It is unclear whether the recorded approval covers only the architecture shape or also these implementation choices. | State explicitly which recommendations the recorded approval covers; keep any unapproved version/build choices unpinned until decided. | OPEN, non-blocking for the approved architecture shape; resolve before finalizing implementation versions/build. |

## Agreed Design Decisions

- Requirements and architecture both record approval dated 2026-10-16. OQ-11 selects local in-memory storage only; data is volatile and lost when the application stops. The previous PostgreSQL proposal is explicitly superseded. No database server, database credentials/connection settings, or deployment setup is required for this implementation. Deployment target selection is deferred.
- OIDC authentication and authenticated-user-ID ownership are consistently required. Client-supplied identity is prohibited. The precise provider/setup and whether OIDC credentials count as “external credentials” remain unresolved as described in DR-10.
- Mandatory owner-scoping applies to plan and child-resource reads, lists, aggregates, and mutations. Unauthenticated requests receive 401; missing and foreign-owned resources receive the same 404 without private-data or existence disclosure; foreign-owned resources do not receive 403.
- Required plan, subject, topic, and session fields and invalid-value rejection are covered. Session local date/time uses the student's profile IANA timezone; DST gaps are rejected, ambiguous times require an explicit UTC offset, and past sessions are allowed. Topic statuses are limited to the three required values; the dashboard aggregates only the student's topics, rounds to the nearest whole percent, and shows 0% when empty.
- Standard security/error handling is addressed through non-disclosing responses and sanitized unexpected errors/logs; no performance or availability targets are specified.
- The approved application shape is a single Spring Boot, Spring MVC, and Thymeleaf web application with responsive desktop, tablet, and mobile support.

## Architecture Updates Required

- No architecture or application files were changed in this review. The requested scope is limited to this review artifact.
- For DR-10, clarify in the configuration and security sections whether the no-external-credentials constraint excludes OIDC provider credentials/access, and document the chosen setup only after that decision.
- For DR-08, add the foreign-owned-parent child-create case to the testability list.
- For DR-09, retain the explicit deferral; add migration/rollback mechanics only if durable storage or deployment later enters scope.
- For DR-11, distinguish the approved architecture shape from any Java/Spring/build recommendations that remain unapproved.

## Residual Risks and Open Questions

- DR-10 resolved: OQ-12 selects a dev-only local form login (two in-memory test users) for this implementation; OIDC provider configuration is deferred.
- DR-11 leaves the approval boundary for Java/Spring versions and build choice unclear. The architecture carries forward compatibility evidence but says it was not independently reverified; Spring Boot 4.1.1 is not established as the latest supported release.
- DR-08 remains a non-blocking test-design omission. DR-09 remains a non-blocking future deployment concern only; neither changes the current local in-memory scope.
- No implementation or tests were run as part of this document-only review.

## Approval

- Decision: No new approval requested or recorded by this review. `architecture.md` records user approval dated 2026-10-16; DR-10 is resolved by OQ-12 and DR-11 remains open as stated above.
- User wording: As recorded in `architecture.md`: “I approve the architecture and design. Proceed with implementation planning only, then stop for my approval before coding.”
- Date: 2026-10-16 (architecture approval record); review date 2026-10-16.
