---
description: "Use when drafting or reviewing requirements, architecture, design review, implementation plan, verification report, or PR artifacts for the staged SDLC workflow."
applyTo:
  - "requirements.md"
  - "architecture.md"
  - "design-review.md"
  - "impl-plan.md"
  - "verification-report.md"
  - "implementation-report.md"
  - "code-review.md"
  - "CHANGELOG.md"
  - "**/requirements.md"
  - "**/architecture.md"
  - "**/design-review.md"
  - "**/impl-plan.md"
  - "**/verification-report.md"
  - "**/implementation-report.md"
  - "**/code-review.md"
---
# SDLC artifact rules

- Use the matching workflow template and keep each artifact focused on its stage.
- Mark artifacts `DRAFT`, `NEEDS DECISION`, `APPROVED`, `BLOCKED`, or `COMPLETE`; never infer approval. Record the exact user approval and date only after it is given.
- Preserve requirement, acceptance-criteria, task, and architecture-decision IDs across artifacts for traceability.
- Separate sourced facts, assumptions, recommendations, unresolved questions, and accepted risks.
- Keep diagrams and tables readable in Markdown. Do not add unsupported claims, fabricated test evidence, or placeholder approvals.