---
name: Start Agentic SDLC
description: "Explicitly start the approval-gated SDLC workflow using the story in the reusable workflow input file; uses Jira/Confluence only when an authorized integration is configured."
argument-hint: "Write the story in .github/skills/agentic-sdlc/user-story-input.md before starting"
agent: SDLC Orchestrator
---
Start the Agentic SDLC workflow. First read [the reusable user story input](../skills/agentic-sdlc/user-story-input.md) using the file-reading tool. Treat that file's content as the story source. If it is missing, empty, or contains only its guidance comment, ask me to write/paste a story there or provide pasted text/a readable local file, and stop without starting requirements work. Jira/Confluence may be used only if an authorized integration is already configured and available in this Copilot harness; otherwise request pasted text or a local export.

After reading a non-empty story source, begin with requirements clarification only. Ask blocking questions and prepare a draft `requirements.md`. Stop and wait for explicit user approval. Do not delegate or execute any later stage in this turn. This prompt starts execution; it is not part of workflow setup or infrastructure validation.