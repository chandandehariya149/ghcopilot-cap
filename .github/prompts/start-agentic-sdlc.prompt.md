---
name: Start Agentic SDLC
description: "Explicitly start the approval-gated SDLC workflow by fetching a Jira story through Atlassian MCP, then pause for requirements clarification and approval."
argument-hint: "Provide a Jira issue key or URL, for example CB-1"
agent: SDLC Orchestrator
---
Start the Agentic SDLC workflow for Jira issue ${input:issue:Enter a Jira issue key or URL (for example CB-1)}. Fetch it with the configured Atlassian MCP read tool before starting requirements work. If only a key is supplied, identify the authorized Jira site using Atlassian resource discovery; if a URL is supplied, use its site. Confirm the issue key, summary, project, type, status, and description to the user. If the issue cannot be fetched, stop and request pasted story text or a local export.

After fetching the Jira issue, begin with requirements clarification only. Ask blocking questions and prepare a draft root `requirements.md` that cites the issue key and URL. Stop and wait for explicit user approval. Do not delegate or execute any later stage in this turn. This prompt starts execution; it is not part of workflow setup or infrastructure validation.
