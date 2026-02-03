---
name: sync-docs-from-session
description: Propose and apply updates to repo docs and agent instructions based on session-verified knowledge. Use when the user asks to sync/update instructions, AGENTS.md, README.md, or docs/* with new, non-obvious information learned in the current session.
---

# Sync Docs From Session

## Overview

Update documentation and instructions only from explicit, session-verified knowledge. Always propose edits first and get user confirmation before applying.

## Required inputs (ask only for missing values)

If the user asks to run the skill without providing knowledge items, draft a suggested list based on:
- Explicit user statements in the current session.
- Files changed or created in this session (paths only, no assumptions).

Present the draft list as a numbered list and ask the user to confirm or edit it before making any file changes.

Provide a short list of knowledge items with evidence from this session.

Preferred input format (YAML list):

- statement: <new or corrected knowledge>
  evidence: <user quote or file path/line observed in this session>
  scope: <optional; files or areas likely impacted>

Example:

- statement: "Release tags do not trigger deployment; push tags only after manual deployment."
  evidence: "User clarified in chat on 2026-02-03"
  scope: "Android release instructions"

## Evidence rules

- Accept only knowledge that is explicitly confirmed in this session (user statement or file content).
- Do not add assumptions or general advice.
- If evidence is missing or ambiguous, ask the user for clarification.

## What to update (repo-wide)

- AGENTS.md (root and project-level)
- .github/instructions/*
- README.md
- docs/*
- Any other documentation files referenced by the user

Prefer to point to existing sources of truth rather than duplicating content.

## Workflow (propose-first)

1) Gather knowledge items and validate evidence.
2) Use rg to find impacted docs and instruction files.
3) Draft a minimal edit plan (file list + change summary).
4) Present proposed edits and ask for confirmation.
5) Apply changes after confirmation, preserving CRLF line endings.
6) Summarize updates and note any files not changed due to missing evidence.

## Editing rules

- Keep changes minimal and scoped to the evidence.
- Follow the most specific instruction file when multiple exist.
- Avoid duplicating content; link to the most specific doc instead.
- Preserve existing structure and tone.
- Maintain CRLF line endings.

## Stop conditions

- If the user does not confirm proposed edits, do not change files.
- If evidence is insufficient, ask for more detail.