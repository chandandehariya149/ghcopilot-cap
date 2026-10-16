# Agentic SDLC Workflow

This workspace contains a GitHub Copilot Agent Mode workflow for taking a user-provided story from clarification to PR preparation. The workflow is defined with native Copilot agents, prompts, instructions, a skill, and a scoped lifecycle hook. Workflow setup is separate from running it against an application repository.

## Start a workflow

Write any story into `.github/skills/agentic-sdlc/user-story-input.md`, then open Copilot Chat in Agent Mode and explicitly run `/start-agentic-sdlc`. The start prompt reads that file first and begins at requirements clarification only. If the file is empty or contains only its guidance comment, the orchestrator asks you to fill it or provide pasted text/a readable local file, then stops.

Jira or Confluence is optional. The workflow can read from those systems only when an authorized integration/MCP server is configured and available to the selected Copilot harness. If it is not configured, provide pasted text or a local export instead. Do not put credentials or tokens in workflow files.

The orchestrator pauses for explicit human approval at requirements, architecture/design, implementation planning, implementation/review disposition, and verification. Use `/continue-agentic-sdlc` after an approval to proceed from the last approved artifact. Do not ask an agent to bypass a gate by invoking it directly.

## Stage sequence and artifacts

| Stage | Specialist | Primary artifact |
| --- | --- | --- |
| Requirements clarification | SDLC Requirements | `requirements.md` |
| Architecture | SDLC Architecture | `architecture.md` |
| Design review | SDLC Design Review | `design-review.md` |
| Implementation planning | SDLC Implementation Planning | `impl-plan.md` |
| Implementation | SDLC Implementation | Application source, tests, and `implementation-report.md` |
| Code review | SDLC Code Review | `code-review.md` with findings and user disposition |
| Testing and verification | SDLC Testing and Verification | `verification-report.md` |
| PR preparation | SDLC PR Preparation | PR description and changelog entry |

The eventual application stack is Java with Spring Boot. The architecture stage selects compatible versions based on the actual requirements and environment. The workflow infrastructure itself has no Java runtime dependency.

## Repository and GitHub prerequisites

Requirements clarification and planning can work from the story and local workspace. Before application implementation, open the intended application repository as the workspace and confirm:

- `git rev-parse --show-toplevel` identifies the intended project root.
- `git remote get-url origin` identifies the exact GitHub owner/repository (or the explicitly approved fork and upstream remotes).
- You identify the intended base branch by name; the workflow must not assume `main` or `master`.
- `git status --short --branch` has no unrelated changes before creating or switching branches. If there are changes, preserve them and decide how to proceed; never discard them automatically.
- You approve the feature branch name and creation from the selected base branch. No branch is created or switched without that approval.
- The local Git client can read the remote. Copilot sign-in in VS Code is separate from GitHub Git credentials. Git operations use your configured HTTPS credential manager or SSH key; `gh auth status --hostname github.com` is an optional check if GitHub CLI is being used.

The workflow can prepare a PR description without GitHub MCP. A remote PR additionally requires the feature branch to be pushed and an account with repository write and pull-request creation permissions. The PR agent prepares content only; creating the remote PR, committing, and pushing each require separate explicit authorization. GitHub MCP is optional for direct GitHub API actions, not required for local files, tests, Git operations, or PR-draft text.

## Setup versus execution

Editing or validating files under `.github/` sets up the workflow; it does not process a story. A run starts only when you explicitly invoke `/start-agentic-sdlc` and provide story text or a local source. Jira/Confluence access is considered only when configured. The safety hook adds context but is not an authorization boundary; follow the documented gates and inspect tool permissions.

## Approval and safety rules

- Only an explicit user request starts a workflow. Merely opening this workspace or discussing the sample does not.
- Record each approval in the corresponding artifact; agents may draft artifacts but must not mark them approved on the user's behalf.
- Approval gates are enforced by the coordinator's staged interaction and recorded artifact checks; the lifecycle hook adds context but is not an authorization boundary. Review tool permissions and workspace trust before using agents.
- Do not implement application code before requirements, architecture/design, and the implementation plan are approved.
- Do not commit, push, publish, or create a remote PR without a separate explicit user request.
- Report blockers, assumptions, failed checks, and missing tools honestly. Never claim a test or review ran when it did not.
- `.github/skills/agentic-sdlc/user-story-input.md` is the reusable, generic story input. Replace its contents for each run; do not treat it as an approval artifact.

## Validate the workflow assets

On Windows PowerShell, run:

```powershell
./.github/scripts/validate-workflow.ps1
```

This checks required files, YAML frontmatter boundaries and required metadata, unique agent names, stage-agent registration, template presence, and Copilot hook JSON. It does not invoke Copilot, create application files, or process a user story.