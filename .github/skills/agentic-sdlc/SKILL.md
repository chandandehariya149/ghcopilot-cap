---
name: agentic-sdlc
description: 'Run or continue the approval-gated software delivery lifecycle for a user story, from requirements clarification through architecture, design review, planning, implementation, code review, verification, and PR preparation. Use only after an explicit user trigger.'
argument-hint: 'User story text or source path; for continuation, identify the approval just given'
---
# Agentic SDLC Procedure

Use this skill only when the user explicitly starts or continues the workflow. The coordinator owns stage order and approval state; delegate stage work to the matching agent in `.github/agents/`.

## Before starting
1. Confirm an explicit trigger and read `.github/skills/agentic-sdlc/user-story-input.md`. If missing, empty, or containing only the guidance comment, ask the user to write/paste a story there or provide pasted text/a readable local file path, then stop. Jira/Confluence may be used only through an explicitly configured, authorized integration available to this harness. Otherwise request pasted text or a local export.
2. Inspect repository state and conventions. Keep workflow setup validation separate from execution against an application repository.
3. Work one stage at a time. Agents draft their outputs; only the user can approve them.

## Ordered stages and gates
1. **Requirements:** clarify gaps, draft `requirements.md`, ask for explicit approval.
2. **Architecture:** after requirements approval, draft `architecture.md`; run design review; record findings/dispositions in `design-review.md`; ask for architecture/design approval.
3. **Implementation planning:** after design approval, draft dependency-ordered `impl-plan.md`; ask for plan approval.
4. **Implementation:** before coding, confirm repository root, intended remote owner/repository, user-selected base branch, worktree status, and explicit approval for any branch creation/switch. Preserve existing changes. After plan approval and preflight, implement only approved tasks in Java/Spring Boot; record changes and check results in `implementation-report.md`; pause for explicit user approval.
5. **Code review:** independent review against requirements, security, error handling, tests, clarity, DRY, dependency safety; record findings in `code-review.md` and obtain user disposition for each before testing.
6. **Testing and verification:** run project-appropriate unit/integration checks and output-content checks; record evidence in `verification-report.md`; ask for explicit approval.
7. **PR preparation:** after verification approval, prepare Summary (2-3 sentences), Changes Made, Test Evidence, Known Limitations, Reviewer Checklist, and a changelog entry when the repository convention calls for one. A remote PR requires the confirmed target repository, approved base/head branches, GitHub write and PR permissions, and separate explicit authorization. GitHub MCP is optional for API operations and is not required for local work or draft PR text.

## Rules
- At every gate, report artifact paths, decisions, actual checks, and the exact approval needed. Use states `NOT STARTED`, `DRAFT`, `NEEDS DECISION`, `APPROVED`, `BLOCKED`, `COMPLETE`.
- Never set `APPROVED` yourself. Record the user's exact approval and date after receiving it.
- If a stage fails or a finding requires changes, return to its owning stage; do not skip ahead.
- Do not claim tests, security checks, approvals, commits, or PR actions that did not happen.
- Use the provided templates under `templates/` for durable artifacts.