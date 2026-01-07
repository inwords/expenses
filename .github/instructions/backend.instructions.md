---
applyTo: "backend/**"
---
# Agent Instructions for CommonEx Backend

## Project Overview

CommonEx backend is a **NestJS** application providing REST and gRPC APIs for the expense sharing platform. It follows Clean Architecture principles with clear separation between domain, use cases, frameworks, and API layers.

## Technology Stack

- **Framework**: NestJS v10
- **Database**: PostgreSQL with TypeORM
- **APIs**: Both gRPC and REST endpoints
- **Observability**: OpenTelemetry tracing and metrics

### Key Dependencies
- `@nestjs/*` packages - Core NestJS framework
- `typeorm` - Database ORM
- `@grpc/grpc-js` - gRPC communication
- `class-validator` - DTO validation
- `class-transformer` - Object transformation

## Architecture

Clean Architecture with layers:
- **API layer** (`api/http`, `api/grpc`) - HTTP/gRPC controllers and DTOs
- **Use cases layer** (`usecases/`) - Business logic orchestration
- **Domain layer** (`domain/`) - Entities, value objects, repository interfaces
- **Frameworks layer** (`frameworks/`) - Infrastructure implementations (TypeORM entities, repositories)

### Key File Locations
- **Main entry**: `src/main.ts`
- **App module**: `src/app.module.ts`
- **Domain entities**: `src/domain/entities/`
- **Value objects**: `src/domain/value-objects/`
- **Use cases**: `src/usecases/`
- **API controllers**: `src/api/http/`, `src/api/grpc/`
- **TypeORM entities**: `src/frameworks/relational-data-service/`
- **Migrations**: `migrations/default/`

## Prerequisites
- **Node.js 24+** (check with `node --version`)
- **PostgreSQL 14+** (check with `psql --version`)
- **npm 10+** (comes with Node.js)
- **Git** for version control

## Environment Setup

### Installation

```bash
cd backend
npm install
```

### Environment Variables

1. Copy example environment file:
   ```bash
   cp example.env .env
   ```

2. Configure required variables in `.env`:
   - Database connection settings
   - API keys and secrets
   - OpenTelemetry configuration
   - See `example.env` for all available options

### Database Setup

1. Ensure PostgreSQL is running
2. Create database (if not exists)
3. Run migrations:
   ```bash
   npm run db:migrate
   ```

## Essential Commands

**Always run commands from the `backend/` directory.**

### Development

```bash
# Start development server with watch mode
npm run start:dev

# Start with debugging enabled
npm run start:debug

# Start production server (after build)
npm run start:prod

# Start basic server
npm run start
```

### Building

```bash
# Build for production
npm run build
```

### Code Quality

```bash
# Run linter
npm run lint

# Format code with Prettier
npm run format
```

### Testing

```bash
# Run all unit tests
npm run test

# Run tests in watch mode
npm run test:watch

# Run tests with coverage report
npm run test:cov

# Run E2E tests
npm run test:e2e

# Debug tests
npm run test:debug
```

### Database

```bash
# Generate new migration from entity changes
npm run db:migrate:new

# Apply pending migrations
npm run db:migrate

# Apply migrations in Docker production
npm run db:migrate:docker_prod

# Create empty migration file
npm run db:migrate:empty

# Drop database schema (⚠️ destructive)
npm run db:drop
```

## Development Workflow

### Adding New Features

1. Define domain entities and value objects in `domain/` layer
2. Create repository interfaces in `domain/abstracts/`
3. Implement repositories in `frameworks/relational-data-service/`
4. Create use cases in `usecases/` layer
5. Add API endpoints in `api/http/` or `api/grpc/`

### Database Changes

1. Update entity models in `frameworks/relational-data-service/`
2. Generate migration: `npm run db:migrate:new`
3. Review generated migration file in `migrations/default/`
4. Apply migration: `npm run db:migrate`
5. Update frontend models if needed

## Coding Standards

- **Entities**: Extend `BaseEntity` (provides id, createdAt, updatedAt)
- **Value Objects**: Extend `ValueObject` base class
- **TypeORM decorators**: Use ONLY in frameworks layer, never in domain layer
- **DTOs**: Use `class-validator` for API input/output validation
- **Database naming**: PostgreSQL uses `snake_case` for tables/columns
- **Layer boundaries**: Domain layer must have no framework dependencies

## Common Tasks

### Adding a New Entity

1. Create domain entity in `src/domain/entities/`
2. Create value objects in `src/domain/value-objects/` if needed
3. Define repository interface in `src/domain/abstracts/`
4. Implement TypeORM entity in `src/frameworks/relational-data-service/`
5. Create repository implementation in frameworks layer
6. Add use cases in `src/usecases/`
7. Create API endpoints in `src/api/http/` or `src/api/grpc/`

### Example: Creating a Simple Entity

```typescript
// 1. Domain entity (src/domain/entities/example.entity.ts)
export class Example extends BaseEntity {
  // domain logic only, no TypeORM decorators
}

// 2. Repository interface (src/domain/abstracts/example.repository.ts)
export interface IExampleRepository {
  findById(id: string): Promise<Example | null>;
}

// 3. TypeORM entity (src/frameworks/relational-data-service/entities/example.entity.ts)
@Entity('examples')
export class ExampleEntity extends BaseEntity {
  @Column()
  name: string;
}
```

## Testing
- **Unit tests**: Jest for use cases and domain logic
- **Integration tests**: Supertest for API endpoints
- **E2E tests**: For critical flows

### Test Structure
- Unit tests: `*.spec.ts` files next to source files
- E2E tests: `test/` directory with `jest-e2e.json` config

## Deployment
- Docker image build with multistage Dockerfile
- Environment variables for configuration
- Health checks via `/health` endpoint
- Migrations run automatically on container startup

## Validation Steps

Before submitting changes, run these validation steps:

```bash
# 1. Run linter
npm run lint

# 2. Run all tests
npm run test

# 3. Run E2E tests
npm run test:e2e

# 4. Build for production
npm run build
```

### Quick Validation (for small changes)

```bash
# Fast validation for minor changes
npm run lint
npm run test
```

## Troubleshooting

### Database Connectivity Issues
- **Check PostgreSQL is running**: `pg_isready` or `psql -U postgres`
- **Verify connection string** in `.env` file
- **Check network connectivity** if using remote database
- **Review migration status**: Check `migrations/` directory

### Migration Issues
- **Migration fails**: Review generated migration file for errors
- **Schema out of sync**: Run `npm run db:migrate` to apply pending migrations
- **Need to rollback**: Manually edit database or create new migration

### Build/Compilation Errors
- **TypeScript errors**: Run `npm run build` to see full error messages
- **Missing dependencies**: Run `npm install`
- **Module resolution**: Check `tsconfig.json` path mappings

### Runtime Issues
- **Environment variables**: Verify `.env` file exists and is configured
- **Port conflicts**: Check if port 3000 (or configured port) is available
- **OpenTelemetry errors**: Review `src/otel.ts` configuration
- **gRPC service errors**: Check `src/expenses.proto` and service definitions

### Common Errors
- **"Cannot find module"**: Run `npm install` and check `node_modules/`
- **"Migration already exists"**: Delete old migration or use `db:migrate:empty` for new one
- **"TypeORM decorator in domain layer"**: Move TypeORM code to frameworks layer
- **"Database connection refused"**: Check PostgreSQL is running and credentials are correct
