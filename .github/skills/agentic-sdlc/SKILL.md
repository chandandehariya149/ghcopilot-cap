---
name: agentic-sdlc
description: 'Run or continue the approval-gated software delivery lifecycle for a Jira user story, from requirements clarification through architecture, design review, planning, implementation, code review, verification, and PR preparation. Use only after an explicit user trigger.'
argument-hint: 'Jira issue key or URL; for continuation, identify the approval just given'
---
# Agentic SDLC Procedure

Use this skill only when the user explicitly starts or continues the workflow. The coordinator owns stage order and approval state; delegate stage work to the matching agent in `.github/agents/`.

## Before starting
1. Confirm an explicit trigger and obtain a Jira issue key or URL. Fetch the issue using the configured Atlassian MCP read tool. Verify and report issue key, summary, project, type, status, and description before drafting requirements. If only a key is provided, identify the authorized site with Atlassian resource discovery; if a URL is provided, use its site. If retrieval fails, request pasted story text or a readable local export and stop.
2. Inspect repository state and conventions. Keep workflow setup validation separate from execution against an application repository.
3. Work one stage at a time. Agents draft their outputs; only the user can approve them.

## Ordered stages and gates
1. **Requirements:** clarify gaps, draft `requirements.md`, ask for explicit approval.
2. **Architecture:** after requirements approval, draft `architecture.md`; run design review; record findings/dispositions in `design-review.md`; ask for architecture/design approval.
3. **Implementation planning:** after design approval, draft dependency-ordered `impl-plan.md`; ask for plan approval.
4. **Implementation:** before coding, confirm repository root, origin owner/repository, user-selected base branch, worktree status, and explicit approval for any branch operation. Preserve user changes. After plan approval and preflight, implement only approved Java/Spring Boot tasks, record results in `implementation-report.md`, and pause for user approval.
5. **Code review:** independent review against approved requirements and security/error/test/clarity/DRY/dependency criteria; record `code-review.md` and obtain disposition for findings.
6. **Testing and verification:** run project-appropriate unit/integration checks and output-content checks; record actual evidence in `verification-report.md`; ask for explicit approval.
7. **PR preparation:** after verification approval, prepare Summary (2-3 sentences), Changes Made, Test Evidence, Known Limitations, Reviewer Checklist, and changelog if repository convention calls for it. Any commit/push/PR action requires separate explicit authorization and confirmed branch/permission state.

## Rules
- At every gate, report artifact paths, decisions, checks actually run, and exact approval needed. Use states `NOT STARTED`, `DRAFT`, `NEEDS DECISION`, `APPROVED`, `BLOCKED`, `COMPLETE`.
- Never set `APPROVED` yourself. Record the user's wording and date only after receiving approval.
- If a stage fails or requires a decision, return to its owning stage; do not skip ahead.
- Do not claim tests, security checks, approvals, commits, or PR actions that did not happen.
- Use templates under `templates/` for durable artifacts.
