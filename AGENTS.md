# CommonEx Agent Guide (Root)

CommonEx is a multi-platform expense sharing application with Android/iOS (KMM), Web (Next.js), and Backend (NestJS) components.

## Scope and precedence

- This file provides repo-wide guidance only. Do not duplicate module instructions.
- Before editing, check for more specific instructions in subdirectories:
  - Android/iOS/KMM: [`.github/instructions/android.instructions.md`](.github/instructions/android.instructions.md) (also referenced via `android/AGENTS.md`)
  - Backend: [`.github/instructions/backend.instructions.md`](.github/instructions/backend.instructions.md) (also referenced via `backend/AGENTS.md`)
  - Web: [`.github/instructions/web.instructions.md`](.github/instructions/web.instructions.md) (also referenced via `web/AGENTS.md`)
  - Infra: [`.github/instructions/infra.instructions.md`](.github/instructions/infra.instructions.md) (also referenced via `infra/AGENTS.md`)
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
- When commands are listed, run them from the relevant module directory; there is no root `package.json`.
- Add comments only when the logic is non-obvious.

## Domain Glossary

- See [`docs/domain.md`](docs/domain.md) for core domain terms and the primary sources of truth for their shape and meaning.

## Testing and validation

- See module-specific instructions above for testing commands and validation steps.

## Documentation hygiene

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
