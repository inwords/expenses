# Project Structure and Agent Guidelines

## Project Overview
CommonEx is a multi-platform expense sharing application built with:
- **Mobile**: Kotlin Multiplatform Mobile (KMM) with Compose Multiplatform
- **Web**: Next.js with Material UI and MobX
- **Backend**: NestJS with gRPC and REST APIs, PostgreSQL database
- **Infrastructure**: Docker-based deployment with Nginx and OpenTelemetry

## Technology Stack

### Backend (NestJS)
- **Framework**: NestJS v10
- **Database**: PostgreSQL with TypeORM
- **APIs**: Both gRPC and REST endpoints
- **Architecture**: Clean Architecture with layers:
  - API Layer (HTTP/gRPC controllers)
  - Use Cases Layer (business logic)
  - Domain Layer (entities, value objects, interfaces)
  - Frameworks Layer (implementations of domain interfaces)
- **Observability**: OpenTelemetry tracing and metrics
- **Key Dependencies**: 
  - @nestjs/* packages
  - typeorm for database operations
  - grpc for microservices communication

### Web Frontend (Next.js)
- **Framework**: Next.js v14 with App Router
- **UI Library**: Material UI v5
- **State Management**: MobX
- **Forms**: react-hook-form with MUI integration
- **Architecture**: Feature-driven with strict folder structure:
  - 2-pages: Page components
  - 3-widgets: Composite UI components
  - 4-features: Business logic components
  - 5-entities: Business entities with stores/services
  - 6-shared: Shared utilities and types

### Mobile (Kotlin Multiplatform)
- **Platform**: Kotlin Multiplatform Mobile (KMM)
- **UI**: Compose Multiplatform
- **Architecture**: Modular with core, feature, and integration modules
- **Networking**: Ktor client
- **Storage**: SQLDelight or Room depending on platform

### Infrastructure
- **Containerization**: Docker
- **Reverse Proxy**: Nginx
- **Observability**: OpenTelemetry Collector
- **Orchestration**: Docker Compose for production

## Coding Standards

### Backend
1. **Clean Architecture**: 
   - Domain layer contains pure business logic with no framework dependencies
   - Frameworks layer contains all infrastructure implementations
   - Use Cases layer orchestrates domain operations
   - API layer handles HTTP/gRPC requests

2. **Entity Design**:
   - Entities should extend BaseEntity with id, createdAt, updatedAt
   - Value Objects should extend ValueObject class
   - Use TypeORM decorators for persistence mapping in frameworks layer

3. **API Development**:
   - REST controllers in `api/http/*`
   - gRPC controllers in `api/grpc/*`
   - DTOs for API input/output validation
   - Use class-validator for request validation

4. **Database**:
   - Use repositories for data access
   - Migrations for schema changes
   - PostgreSQL naming convention: snake_case for tables/columns

### Web Frontend
1. **Folder Structure**:
   - Strict adherence to feature-sliced design methodology
   - Pages (routes) in 2-pages
   - Widgets (composite components) in 3-widgets
   - Features (business logic) in 4-features
   - Entities (business entities) in 5-entities
   - Shared utilities in 6-shared

2. **State Management**:
   - Use MobX stores for state management
   - Services for API interactions
   - Stores should be injected via context or direct imports

3. **Component Design**:
   - Use MUI components as base
   - Custom components should follow MUI styling patterns
   - Form components should integrate with react-hook-form

### Mobile
1. **Multiplatform Structure**:
   - Common code in `src/commonMain`
   - Platform-specific in `src/androidMain` and `src/iosMain`
   - Shared modules for core functionality

2. **Architecture**:
   - MVVM pattern with ViewModel classes
   - Repository pattern for data access
   - Dependency injection via custom locator

## Development Workflow

### Backend Development
1. **Adding New Features**:
   - Define domain entities and value objects
   - Create repository interfaces in domain layer
   - Implement repositories in frameworks layer
   - Create use cases in usecases layer
   - Add API endpoints in api layer

2. **Database Changes**:
   - Create migrations using `npm run db:migrate:new`
   - Update entity models accordingly
   - Test with `npm run db:migrate`

3. **Testing**:
   - Unit tests for use cases and domain logic
   - Integration tests for API endpoints
   - E2E tests for critical flows

### Web Development
1. **Component Creation**:
   - Follow the feature-sliced structure
   - Create entities first if introducing new business concepts
   - Build features on top of entities
   - Compose widgets from features
   - Connect to pages

2. **State Management**:
   - Create MobX stores for complex state
   - Services for API calls
   - Use React Context for store distribution

3. **Styling**:
   - Use MUI theme for consistent styling
   - Custom components should extend MUI components
   - CSS modules for component-specific styles

### Mobile Development
1. **Feature Development**:
   - Create feature modules in shared directory
   - Implement common functionality in commonMain
   - Add platform-specific code in respective directories
   - Integrate with Android/iOS apps

2. **Dependencies**:
   - Add shared module dependencies in feature module build files
   - Use version catalogs for dependency management

## Testing Guidelines

### Backend
- **Unit Tests**: Jest for use cases and domain logic
- **Integration Tests**: Supertest for API endpoints
- **Test Commands**:
  - `npm run test` - run all tests
  - `npm run test:watch` - watch mode
  - `npm run test:e2e` - end-to-end tests

### Web
- **Unit Tests**: Jest for utility functions and services
- **Component Tests**: React Testing Library
- **Test Commands**:
  - `npm run test` - run tests
  - `npm run test:watch` - watch mode

## Deployment

### Backend
- Docker image build with multistage Dockerfile
- Environment variables for configuration
- Health checks via /health endpoint

### Web
- Static export or server-side rendering
- Docker image for deployment
- Nginx serving static files

### Infrastructure
- Docker Compose for multi-container setup
- Nginx reverse proxy configuration
- OpenTelemetry collector for observability

## Common Tasks

### Adding a New Entity
1. Backend:
   - Create domain entity and value objects
   - Define repository interface
   - Implement TypeORM entity
   - Create repository implementation
   - Add use cases
   - Create API endpoints

2. Web:
   - Create entity folder with types, services, stores
   - Add API service methods
   - Create MobX store
   - Build UI components

3. Mobile:
   - Create entity in shared module
   - Add data models and repositories
   - Implement platform-specific storage if needed

### Database Migration
1. Update entity models
2. Generate migration: `npm run db:migrate:new`
3. Review generated migration file
4. Apply migration: `npm run db:migrate`
5. Update frontend models if needed

## Troubleshooting

### Backend Issues
- Check database connectivity
- Verify environment variables
- Review OpenTelemetry configuration
- Check gRPC service definitions

### Web Issues
- Clear Next.js cache: `rm -rf .next`
- Check API endpoint URLs
- Verify MobX store initialization
- Review MUI theme configuration

### Mobile Issues
- Clean build folders
- Check shared module dependencies
- Verify Ktor client configuration
- Review platform-specific implementations
