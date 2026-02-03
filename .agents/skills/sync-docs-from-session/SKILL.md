---
name: sync-docs-from-session
description: Propose and apply updates to repo docs and agent instructions based on session-verified knowledge. Use when the user asks to sync/update instructions, AGENTS.md, README.md, or docs/* with new, non-obvious information learned in the current session.
---

# Sync Docs From Session

## Purpose

Sync documentation and instructions to session-verified knowledge. Propose edits first; apply only after confirmation.

## Inputs (ask only for missing values)

Preferred input format (YAML list):

- statement: <new or corrected knowledge>
  evidence: <user quote or file path/line observed in this session>
  scope: <optional; files or areas likely impacted>

If the user asks to run the skill without providing knowledge items, draft a suggested list with two sections:

- Tier A (explicit, auto-accepted)
- Tier B (derived, needs confirmation)

Use these sources:

- Explicit user statements in the current session.
- Files changed or created in this session (paths only, no assumptions).
- Doc discovery results (new README/guides and tooling folders).

Present the draft list as a numbered list and ask the user to confirm or edit it before making any file changes.

## Evidence tiers

**Tier A: Explicit (auto-accepted)**

- Direct user statements in this session
- Files created, edited, or removed in this session

**Tier B: Derived (confirm before changes)**

- Inferences directly observable from files (no speculation)
- Examples:
    - New tool folder or README implies the tool is part of the workflow
    - New README implies a parent doc should link to it
    - Moved files imply backlinks should be updated

Do not apply Tier B changes unless confirmed.

## Pattern discovery (optional, derived)

Propose **code patterns** or **architectural patterns** only when all are true:

- The pattern appears in files edited in this session.
- It appears at least twice (same file or multiple files).
- The pattern is specific and actionable (not generic advice).
- Evidence includes exact file paths and a short snippet reference.

Otherwise, do not propose the pattern.

## Doc discovery (before proposing edits)

1) List new or updated docs in the session (README.md, docs/*, AGENTS.md).
2) Identify new tooling folders (e.g., android/marathon, android/gradle/profiler).
3) Choose the most appropriate level doc to link from:
    - Project AGENTS.md for project-specific docs
    - Root AGENTS.md only for cross-project docs
4) Draft backlink proposals as Tier B items and ask for confirmation.

## What to update (repo-wide)

- AGENTS.md (root and project-level)
- .github/instructions/*
- README.md
- docs/*
- Any other documentation files referenced by the user

Prefer links to a single source of truth over duplicated content.

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
- Link propagation: when a new README or guide is added in a subfolder, add a short pointer from the most appropriate level doc.
- Preserve existing structure and tone.
- Maintain CRLF line endings.

## Stop conditions

- If the user does not confirm proposed edits, do not change files.
- If evidence is insufficient, ask for more detail.