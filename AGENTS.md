# CommonEx Agent Guide (Root)

CommonEx is a multi-platform expense sharing application with Android/iOS (KMM), Web (Next.js), and Backend (NestJS) components.

## Scope and precedence
- This file provides repo-wide guidance only. Do not duplicate module instructions.
- Before editing, check for more specific instructions in subdirectories:
  - Android/iOS/KMM: [`.github/instructions/android.instructions.md`](.github/instructions/android.instructions.md) (also referenced via `android/AGENTS.md`)
  - Backend: [`.github/instructions/backend.instructions.md`](.github/instructions/backend.instructions.md)
  - Web: [`.github/instructions/web.instructions.md`](.github/instructions/web.instructions.md)
  - Infra: [`.github/instructions/infra.instructions.md`](.github/instructions/infra.instructions.md)
- If instructions conflict, follow the most specific and recent document.

## Repository map
- `android/` KMM + Android and iOS apps
- `backend/` NestJS services
- `web/` Next.js app
- `infra/` Docker/Nginx/OpenTelemetry
- `.github/instructions/` scoped instructions
- `.github/workflows/` CI/CD pipelines

## Cross-cutting standards
- Line endings: CRLF across all files.
- Keep changes minimal; preserve architecture and conventions already in place unless improving them.
- Avoid editing generated artifacts and build outputs (e.g., `.next/`, `build/`).
- When commands are listed, run them from the relevant module directory; there is no root `package.json`.

## Testing and validation
- See module-specific instructions above for testing commands and validation steps.

## Documentation hygiene
- Keep instructions short and linked rather than duplicated.
- If you add new scoped instructions, update this file with a pointer.
