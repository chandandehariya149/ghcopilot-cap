# Daybook TODO App

Daybook is a small Spring Boot application with a plain browser interface and a shared, in-memory task list.

## Run locally

Requirements: Java 17 or later and Maven 3.6.3 or later.

From the repository root, run:

```powershell
mvn test
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080). The list is shared by clients connected to the same running process and is cleared when the application restarts. No account or database is used.

## Use the app

- Add a task with the entry field. Surrounding whitespace is trimmed; blank tasks and case-insensitive duplicates are rejected.
- Toggle a task's Done checkbox to complete or reopen it.
- Delete a task with its Delete button.
- The completed/total count and empty state update with the list.

## REST API

All endpoints use JSON except `DELETE` success, which has no response body.

| Method and path | Behavior | Success | Expected errors |
| --- | --- | --- | --- |
| `GET /api/todos` | Return the shared list; an empty list is `[]`. | `200` | `500` for unexpected failures |
| `POST /api/todos` | Create `{ "text": "..." }`; text is trimmed. | `201` | `400` invalid/blank request; `409` duplicate |
| `PUT /api/todos/{id}` | Set `{ "completed": true }` or `false`. | `200` | `400` invalid completion; `404` unknown ID |
| `DELETE /api/todos/{id}` | Remove the task. | `204` | `404` unknown ID |

Expected errors return a JSON body with `code` and `message` fields. The application stores tasks in process memory, so a restart clears the list.

## Agentic SDLC Workflow

This workspace contains a GitHub Copilot Agent Mode workflow for taking a Jira user story from clarification to PR preparation. The workflow is defined with native Copilot agents, prompts, instructions, a skill, and a scoped lifecycle hook. Workflow setup is separate from running it against an application repository.

### Start a workflow

Open Copilot Chat in Agent Mode and explicitly run `/start-agentic-sdlc` with a Jira issue key or URL (for example, `CB-1`). The start prompt fetches the issue through the configured Atlassian MCP and begins at requirements clarification only. If Jira retrieval fails, the workflow stops and asks for pasted story text or a local export.

Jira is the primary workflow source. Confluence is available only through an authorized configured integration. If Jira cannot be reached, provide pasted text or a local export. Never put credentials or tokens in workflow files.

The orchestrator pauses for explicit human approval at requirements, architecture/design, implementation planning, implementation/review disposition, and verification. Use `/continue-agentic-sdlc` after an approval to proceed from the last approved artifact. Do not ask an agent to bypass a gate by invoking it directly.

### Stage sequence and artifacts

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

### Repository and GitHub prerequisites

Requirements clarification and planning can work from the fetched issue and local workspace. Before application implementation, open the intended application repository as the workspace and confirm:

- `git rev-parse --show-toplevel` identifies the intended project root.
- `git remote get-url origin` identifies the exact GitHub owner/repository (or the explicitly approved fork and upstream remotes).
- You identify the intended base branch by name; the workflow must not assume `main` or `master`.
- `git status --short --branch` has no unrelated changes before creating or switching branches. If there are changes, preserve them and decide how to proceed; never discard them automatically.
- You approve the feature branch name and creation from the selected base branch. No branch is created or switched without that approval.
- The local Git client can read the remote. Copilot sign-in in VS Code is separate from GitHub Git credentials. Git operations use your configured HTTPS credential manager or SSH key; `gh auth status --hostname github.com` is an optional check if GitHub CLI is being used.

The workflow can prepare a PR description without GitHub MCP. A remote PR additionally requires the feature branch to be pushed and an account with repository write and pull-request creation permissions. The PR agent prepares content only; creating the remote PR, committing, and pushing each require separate explicit authorization. GitHub MCP is optional for direct GitHub API actions, not required for local files, tests, Git operations, or PR-draft text.

### Setup versus execution

Editing or validating files under `.github/` sets up the workflow; it does not process a story. A run starts only when you explicitly invoke `/start-agentic-sdlc` with a Jira issue key or URL. The prompt fetches the issue only through the configured Atlassian MCP. The safety hook adds context but is not an authorization boundary; follow the documented gates and inspect tool permissions.

### Approval and safety rules

- Only an explicit user request starts a workflow. Merely opening this workspace or discussing a story does not.
- Record each approval in the corresponding artifact; agents may draft artifacts but must not mark them approved on the user's behalf.
- Approval gates are enforced by the coordinator's staged interaction and recorded artifact checks; the lifecycle hook adds context but is not an authorization boundary. Review tool permissions and workspace trust before using agents.
- Do not implement application code before requirements, architecture/design, and the implementation plan are approved.
- Do not commit, push, publish, or create a remote PR without a separate explicit user request.
- Report blockers, assumptions, failed checks, and missing tools honestly. Never claim a test or review ran when it did not.
- Jira issues are source input; generated, human-approved SDLC artifacts are separate root-level files. No user story is bundled in the workflow infrastructure.

### Validate the workflow assets

On Windows PowerShell, run:

```powershell
./.github/scripts/validate-workflow.ps1
```

This checks required files, YAML frontmatter boundaries and required metadata, unique agent names, stage-agent registration, template presence, and Copilot hook JSON. It does not invoke Copilot, create application files, or process a user story.