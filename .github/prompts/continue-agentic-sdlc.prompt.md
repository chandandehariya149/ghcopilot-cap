---
name: Continue Agentic SDLC
description: "Continue an already-started Agentic SDLC workflow after the user explicitly approves its current gate; verify approvals and prerequisites first."
argument-hint: "State the artifact or stage you are approving"
agent: SDLC Orchestrator
---
Continue the existing Agentic SDLC workflow only from its last completed stage. First verify the user's explicit approval and all required artifacts. Before implementation, also verify the confirmed repository, remote, base branch, worktree decision, and approved feature-branch action. Before PR actions, reconfirm target repository, base/head branches, permissions, and explicit authorization. If the message does not clearly approve the current gate, ask what they want changed or approved and stop. Delegate exactly the next eligible stage, then return its result and pause at the next human gate. Never skip stages or infer approval from silence.