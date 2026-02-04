# CommonEx Agent Guide (Root)

CommonEx is a multi-platform expense sharing application with Android/iOS (KMM), Web (Next.js), and Backend (NestJS) components.

## Scope and precedence

- This file provides repo-wide guidance only. Do not duplicate project instructions.
- Before editing, check for more specific instructions in subdirectories:
  - Android/iOS/KMM: [`android/AGENTS.md`](android/AGENTS.md) (scoped by `.github/instructions/android.instructions.md`)
  - Backend: [`backend/AGENTS.md`](backend/AGENTS.md) (scoped by `.github/instructions/backend.instructions.md`)
  - Web: [`web/AGENTS.md`](web/AGENTS.md) (scoped by `.github/instructions/web.instructions.md`)
  - Infra: [`infra/AGENTS.md`](infra/AGENTS.md) (scoped by `.github/instructions/infra.instructions.md`)
- If instructions conflict, follow the most specific and recent document.

## Repository map

- `android/` KMM + Android and iOS apps
- `backend/` NestJS services
- `web/` Next.js app
- `infra/` Docker/Nginx/OpenTelemetry
- `.github/instructions/` scoped agent instructions
- `.github/workflows/` CI/CD pipelines

## Cross-cutting standards

- Use CRLF line endings in all files.
- Keep changes minimal; preserve architecture and conventions already in place unless improving them.
- Avoid editing generated and build output files, `.env` and secrets (e.g., `.next/`, `build/`).
- When commands are listed, run them from the relevant project directory; there is no root `package.json`.
- Add comments only when the logic is non-obvious.

## Domain Glossary

- See [`docs/domain.md`](docs/domain.md) for core domain terms and the primary sources of truth for their shape and meaning.

## Testing and validation

- See project-specific instructions above for testing commands and validation steps.

## Documentation hygiene

- Use .agents/skills/sync-docs-from-session to propose doc/instruction updates from session-verified knowledge.
- Repo skills: `android/.agents/skills/add-ui-test` (Android UI tests), `android/.agents/skills/prepare-android-release` (Android release SOP), `.agents/skills/sync-docs-from-session` (doc sync).
- Skills may include `agents/openai.yaml` for UI metadata/discoverability.
- Keep instructions short and linked rather than duplicated.
- If you add new scoped instructions, update this file with a pointer.
- Keep instructions up to date with any code or process changes.

## Workflow agent rules

- Identify the target project first: `android/`, `backend/`, `web/`, or `infra/`.
- Read `AGENTS.md` and the project instruction file; follow the most specific guidance.
- Important: try to fix things at the cause, not the symptom. Keep changes minimal and focused.
- For non-trivial work, propose a short plan before editing.
- Stop and ask clarifying questions if you are less that 80% sure about the task.
- Call out server-client mismatches before changing contracts.
