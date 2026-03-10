# SaaS AI Editor Backend

Backend API for a SaaS AI-powered code editor.  
This portfolio project focuses on clean backend architecture (Controller -> Service -> Repository -> Mapper), multi-tenant project management, team collaboration, and subscription-oriented data modeling.

## Portfolio Snapshot

### What is implemented now
- Project CRUD flow with ownership checks and soft-delete.
- Member listing and invitation flow for projects.
- Access checks at repository/service layer for protected project operations.
- DTO-based API contracts using Java records.
- MapStruct mappers for entity-to-response transformations.

### What is scaffolded (in progress)
- Authentication logic (`signup`, `login`, profile retrieval service).
- File tree/content service.
- Billing/subscription service.
- Usage tracking service responses.
- Member role update and member removal logic.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Web + Spring Data JPA
- PostgreSQL
- MapStruct
- Lombok
- Maven Wrapper (`./mvnw`)

## Architecture

### Layered design
- `controller`: REST endpoints and HTTP response handling.
- `service`: business logic contracts and implementations.
- `repository`: JPA repositories + JPQL access filters.
- `mapper`: MapStruct DTO mappings.
- `entity`: persistence models for core SaaS domain.

### Current authorization pattern
- Controllers currently use a placeholder user id (`Long userId = 1L`) to simulate authenticated context.
- Service and repository methods are already written with `userId` parameters, so swapping to real auth context is straightforward.

## API Endpoints (Current State)

Status legend:
- `Implemented`: meaningful service-layer logic exists.
- `Scaffolded`: endpoint exists but service returns `null` or is not complete.

| Method | Endpoint | Status | Notes |
|---|---|---|---|
| POST | `/api/auth/signup` | Scaffolded | Contract ready (`SignupRequest -> AuthResponse`) |
| POST | `/api/auth/login` | Scaffolded | Contract ready (`LoginRequest -> AuthResponse`) |
| GET | `/api/auth/me` | Scaffolded | Uses `UserService`, logic pending |
| GET | `/api/projects` | Implemented | Returns projects accessible by current user (owner scope currently) |
| GET | `/api/projects/{id}` | Implemented | Access-checked project fetch |
| POST | `/api/projects` | Implemented | Creates project with current user as owner |
| PATCH | `/api/projects/{id}` | Implemented | Owner-only project rename |
| DELETE | `/api/projects/{id}` | Implemented | Owner-only soft delete (`deletedAt`) |
| GET | `/api/projects/{projectId}/members` | Implemented | Returns owner + invited members |
| POST | `/api/projects/{projectId}/members` | Implemented | Owner-only invite flow with validations |
| PATCH | `/api/projects/{projectId}/members/{memberId}` | Scaffolded | Method exists, logic pending |
| DELETE | `/api/projects/{projectId}/members/{memberId}` | Scaffolded | Method exists, logic pending |
| GET | `/api/projects/{projectId}/files` | Scaffolded | File tree contract exists |
| GET | `/api/projects/{projectId}/files/{*path}` | Scaffolded | File content contract exists |
| GET | `/api/plans` | Scaffolded | Plan listing contract exists |
| GET | `/api/me/subscription` | Scaffolded | Subscription contract exists |
| POST | `/api/stripe/checkout` | Scaffolded | Checkout session response contract exists |
| POST | `/api/stripe/portal` | Scaffolded | Stripe portal response contract exists |
| GET | `/api/usage/today` | Scaffolded | Usage DTO exists |
| GET | `/api/usage/limits` | Scaffolded | Plan limit DTO exists |

## Service Layer Highlights

### Project service
- `getProjectsByUserId`: returns user-accessible projects using repository query + mapper conversion.
- `getProjectById`: validates access before returning project DTO.
- `createProject`: validates owner user, creates private project by default.
- `updateProject`: allows update only for owner.
- `softDeleteProject`: owner-only delete with `deletedAt` timestamp.

### Project member service
- Validates access before member operations.
- Adds project owner as `OWNER` in member list response.
- `inviteMember` validates:
  - Inviter is project owner.
  - Invitee exists.
  - Owner cannot invite themselves.
  - Duplicate membership is blocked.

## Domain Model

Core entities modeled in the codebase:
- `User`
- `Project`
- `ProjectMember` (`ProjectMemberId` composite key)
- `ProjectFile`
- `ChatSession`
- `ChatMessage`
- `Plan`
- `Subscription`
- `Preview`
- `UsageLog`

Enums:
- `ProjectRole`: `EDITOR`, `VIEWER`, `OWNER`

## Local Setup

### Prerequisites
- Java 21+
- PostgreSQL instance


Update these values for your environment before running.

### Run locally
```bash
./mvnw clean compile
./mvnw spring-boot:run
```

### Run tests
```bash
./mvnw test
```

Note: tests require a reachable PostgreSQL instance matching your Spring datasource settings.

## Roadmap

- Implement authentication and JWT-based user context (replace hardcoded `userId = 1L`).
- Complete file service and object storage integration.
- Complete billing (Stripe checkout + customer portal + subscription sync).
- Implement usage tracking and quota enforcement endpoints.
- Add controller/service integration tests with isolated test database profile.

## Why This Project

This backend is designed as a strong foundation for a production-style SaaS editor:
- clear separation of concerns,
- explicit DTO contracts,
- security-aware service signatures,
- and a realistic domain model for collaboration + billing + AI usage.
