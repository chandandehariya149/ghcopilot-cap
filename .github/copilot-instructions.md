# Workspace-wide Copilot instructions

- This repository defines an Agentic SDLC workflow. Do not start, resume, or simulate that workflow unless the user explicitly asks.
- A story is never bundled or assumed. At every explicit workflow start, obtain pasted story text or a readable local file path; if absent, ask and stop. Use Jira/Confluence only through an explicitly configured, authorized integration; otherwise request pasted text or a local export.
- Follow the coordinator's stage order and approval gates. A specialist may draft only its assigned stage and must not treat a draft, a handoff, or silence as approval.
- Keep workflow setup changes separate from future application changes. Do not add sample application code, requirements, or fabricated user-story details to this workspace.
- The eventual application target is Java with Spring Boot. Establish exact versions and project structure during approved architecture work, not during infrastructure setup.
- Before implementation, require the intended Git repository root, confirmed `origin` owner/repository, named base branch, clean-worktree decision, and explicit approval of any feature-branch creation/switch. Never assume a default branch or discard user changes.
- Copilot sign-in and GitHub Git credentials are separate. Do not require GitHub MCP for local work, Git, tests, or PR-draft text; remote PR actions need pushed branch access and explicit authorization.
- Never claim a command, test, review, PR, or approval occurred unless it actually did.
- Do not commit, push, publish, or create a remote PR without a distinct explicit user request.