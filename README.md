# SaaS AI Editor — Backend

> Backend API for a multi-tenant, AI-powered SaaS code editor — built to demonstrate production-style backend architecture: clean layering, stateless JWT security, an externalized 12-factor configuration model, and a realistic collaboration + billing + AI-usage domain.

<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-4.0-6DB33F?logo=springboot&logoColor=white">
  <img alt="Spring Security" src="https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white">
  <img alt="PostgreSQL" src="https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white">
  <img alt="MapStruct" src="https://img.shields.io/badge/MapStruct-1.6-E10098">
  <img alt="Maven" src="https://img.shields.io/badge/Maven-Wrapper-C71A36?logo=apachemaven&logoColor=white">
</p>

---

## Table of Contents
- [Overview](#overview)
- [Highlights for Reviewers](#highlights-for-reviewers)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [System Design & Design Decisions](#system-design--design-decisions)
- [Authentication Deep-Dive](#authentication-deep-dive)
- [Domain Model](#domain-model)
- [API Reference](#api-reference)
- [Configuration & Secrets (12-Factor)](#configuration--secrets-12-factor)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Roadmap](#roadmap)

---

## Overview

This service is the backend for a SaaS product where users create coding **projects**, collaborate with **team members**, run AI **chat sessions** against their code, spin up live **previews**, and are billed through **subscription plans** with **usage quotas**.

The codebase is intentionally structured the way a real product team would build it:

```
Controller  →  Service (interface + impl)  →  Repository  →  Database
                     │
                     └──  Mapper (MapStruct)  →  DTO (Java records)
```

Each layer has a single responsibility, DTOs form explicit API contracts, and cross-cutting concerns (security, error handling, configuration) are isolated into dedicated packages.

---

## Highlights for Reviewers

These are the parts of the codebase worth looking at in an interview context:

- **🔐 Stateless JWT authentication** — full signup/login flow on Spring Security 6/7 with `BCrypt`, a `UserDetailsService`, and an `AuthenticationManager` sourced from Spring's `AuthenticationConfiguration`.
- **🛂 Method-level RBAC** — project actions are guarded by `@PreAuthorize` expressions backed by a `SecurityExpressions` bean, mapping `ProjectRole` (`OWNER`/`EDITOR`/`VIEWER`) onto fine-grained `ProjectPermission`s (`VIEW`/`EDIT`/`DELETE`/`MANAGE_MEMBERS`).
- **📦 DTO-first API contracts** — every request/response is a Java `record`; entities never leak across the HTTP boundary. MapStruct generates the entity↔DTO mappings at compile time.
- **⚙️ 12-Factor configuration** — config is committed with `${ENV:default}` placeholders; real secrets live only in a git-ignored profile file locally and in environment variables / a secrets manager in production. The app **fails fast** if a required secret is missing.
- **🛡️ Centralized error handling** — a `@RestControllerAdvice` translates domain exceptions into a consistent `ApiError` JSON shape.
- **🗄️ Realistic, indexed domain model** — composite keys (`ProjectMember`), soft deletes, auditing timestamps, and query-tuned indexes on hot paths.

> **Status legend** — ✅ Implemented · 🟡 Partial · ⬜ Scaffolded (contract + wiring exist, business logic pending).

---

## Tech Stack

| Concern | Choice |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0 (Web, Data JPA, Security) |
| Auth | JJWT 0.13, BCrypt, stateless sessions |
| Persistence | PostgreSQL + Hibernate/JPA |
| Mapping | MapStruct 1.6 |
| Boilerplate | Lombok |
| Build | Maven Wrapper (`./mvnw`) |
| CI | GitHub Actions (`.github/workflows/ci.yml`) |

---

## System Architecture

### Layered design & request flow

```mermaid
flowchart LR
    Client([Client / Frontend])

    subgraph Spring Boot Application
        direction TB
        Filter[Security Filter Chain<br/>stateless · BCrypt · JWT]
        Controller[Controllers<br/>REST endpoints]
        Service[Services<br/>business logic]
        Mapper[MapStruct Mappers<br/>entity ⇄ DTO]
        Repo[Spring Data Repositories]
        Advice[GlobalExceptionHandler<br/>RestControllerAdvice]
    end

    DB[(PostgreSQL)]

    Client -->|HTTP + Bearer token| Filter
    Filter --> Controller
    Controller --> Service
    Service --> Repo
    Service --> Mapper
    Repo --> DB
    Controller -.errors.-> Advice
    Service -.errors.-> Advice
    Advice -->|ApiError JSON| Client
    Controller -->|DTO JSON| Client
```

### Package responsibilities

| Package | Responsibility |
|---|---|
| `controller` | REST endpoints, request binding, HTTP status mapping |
| `service` / `service.impl` | Business logic behind interfaces (testable, swappable) |
| `repository` | Spring Data JPA repositories + JPQL access filters |
| `mapper` | Compile-time MapStruct entity ⇄ DTO conversions |
| `dto` | Immutable API contracts (`record`s), grouped by domain |
| `entity` | JPA persistence models |
| `enums` | Domain enums (`ProjectRole`, `MessageRole`, …) |
| `security` | JWT, `SecurityConfig`, `SecurityExpressions` (`@PreAuthorize` RBAC) |
| `error` | `ApiError`, domain exceptions, global handler |

---

## System Design & Design Decisions

The design follows a **modular monolith**: a single deployable Spring Boot service, internally split into clear layers and domain packages. At this stage (single service, pre-traffic), a monolith keeps iteration fast while the layering leaves a clean seam to extract services later if needed.

Key decisions are recorded below in **decision → reasoning → trade-off** form.

### 1. Layered architecture with interface-driven services
- **Decision:** `Controller → Service (interface + impl) → Repository`, with mappers isolating DTO conversion.
- **Reasoning:** Single responsibility per layer; services are programmed to interfaces so logic is mockable in tests and implementations are swappable.
- **Trade-off:** More files/indirection than putting logic in controllers — accepted for testability and clarity.

### 2. DTO-first contracts using Java records
- **Decision:** Every request/response is an immutable `record`; entities never cross the HTTP boundary.
- **Reasoning:** Decouples the API contract from the persistence schema, prevents accidental leakage of fields (e.g. password hashes), and gives stable, versionable payloads.
- **Trade-off:** Requires mapping code — delegated to **MapStruct** (compile-time, no reflection cost).

### 3. `User` implements `UserDetails` directly
- **Decision:** The JPA `User` entity implements Spring Security's `UserDetails`, and `UserServiceImpl` implements `UserDetailsService`.
- **Reasoning:** Removes an extra adapter layer and a separate service class; the authenticated principal *is* the domain user, so downstream code casts straight to `User` without unwrapping.
- **Trade-off:** The persistence model gains a light Spring Security dependency — accepted for a simpler, flatter object graph.

### 4. `AuthenticationManager` from `AuthenticationConfiguration`
- **Decision:** Expose the `AuthenticationManager` via Spring's `AuthenticationConfiguration.getAuthenticationManager()` instead of hand-building a `DaoAuthenticationProvider` + `ProviderManager`.
- **Reasoning:** Lets the framework wire the provider from the registered `UserDetailsService` + `PasswordEncoder`, cutting boilerplate and avoiding a self-referential `ProviderManager` pitfall.
- **Trade-off:** Slightly less explicit than declaring each bean by hand — accepted for the framework-idiomatic path.

### 5. Stateless, token-based security
- **Decision:** `SessionCreationPolicy.STATELESS`, `BCrypt` password hashing, JWT bearer tokens.
- **Reasoning:** No server-side session state → horizontally scalable; BCrypt is the standard adaptive password hash.
- **Trade-off:** Token revocation is harder than server sessions (mitigated later with short expiry + refresh tokens).

### 6. Role-based ownership via `ProjectMember` + method-level RBAC
- **Decision:** Model ownership/permissions through a `ProjectMember` join entity (`OWNER`/`EDITOR`/`VIEWER`) with a composite key, rather than an `ownerId` column on `Project`. Each `ProjectRole` maps to a set of fine-grained `ProjectPermission`s, and service methods are guarded with `@PreAuthorize("@securityExpressions.canEditProject(#projectId)")`-style checks.
- **Reasoning:** Supports true multi-member collaboration and per-user roles from day one; centralizing the role→permission mapping and enforcing it declaratively keeps authorization logic out of the business methods.
- **Trade-off:** Slightly more complex queries (composite key, joins) than a single owner column, plus a per-call role lookup — accepted for expressive, centralized access control.

### 7. Soft deletes + auditing as conventions
- **Decision:** `deletedAt` for soft deletion and `@CreationTimestamp`/`@UpdateTimestamp` for auditing across core entities; hot query paths are backed by composite indexes.
- **Reasoning:** Preserves history/recoverability and supports auditability; indexes keep "active rows ordered by recency" reads fast.
- **Trade-off:** Queries must consistently filter `deletedAt IS NULL`.

### 8. Centralized error handling
- **Decision:** A `@RestControllerAdvice` (`GlobalExceptionHandler`) maps domain exceptions to a single `ApiError` JSON shape.
- **Reasoning:** Consistent error contract for clients; no try/catch noise in controllers.
- **Trade-off:** Exceptions must be modeled deliberately (`BadRequestException`, `ResourceNotFoundException`).

### 9. Externalized, fail-fast configuration
- **Decision:** Commit config with `${ENV:default}` placeholders; secrets live only in a git-ignored local profile / environment variables; required secrets have **no default**.
- **Reasoning:** 12-factor portability across environments; a misconfigured deploy fails at startup rather than running insecurely.
- **Trade-off:** A one-time local setup step (copy the example file).

### Current limitations (intentionally honest)
- **Authorization enforcement is still expanding.** The request-side JWT filter now validates tokens and populates the `SecurityContext`, and project view/edit/delete plus member view/management are guarded by `@PreAuthorize` RBAC. File, billing, and usage endpoints still need their own permission checks.
- **No persistence-level tenant isolation yet** beyond service/repository access checks and method-level RBAC.
- **No integration tests** against a real database profile yet (planned via Testcontainers).

### How this scales from here
- Stateless auth ⇒ run **N instances behind a load balancer** with no sticky sessions.
- File storage targets **object storage (MinIO/S3)**, keeping large blobs out of the database.
- Previews are designed around **Kubernetes** (namespace/pod per preview).
- The clean layer boundaries make it straightforward to extract a high-traffic concern (e.g. AI usage) into its own service later.

---

## Authentication Deep-Dive

Authentication is the most complete vertical slice and showcases deliberate Spring Security design choices.

### Design decisions

- **The principal is the domain user.** `User` implements `UserDetails` directly and `UserServiceImpl` implements `UserDetailsService`, so the authenticated principal *is* the `User` — no adapter to unwrap.
- **Framework-wired authentication.** The `AuthenticationManager` is obtained from Spring's `AuthenticationConfiguration`, which assembles the provider from the registered `UserDetailsService` + `BCryptPasswordEncoder` — minimal boilerplate, no self-referential `ProviderManager` pitfall.
- **Method-level RBAC.** Beyond authentication, project actions are authorized with `@PreAuthorize` expressions resolved by a `SecurityExpressions` bean that maps `ProjectRole` to `ProjectPermission`s.
- **Stateless & hashed.** Sessions are `STATELESS`; passwords are hashed with `BCrypt`; the API is consumed with a `Bearer` JWT.

### Login flow

```mermaid
sequenceDiagram
    autonumber
    participant C as Client
    participant AC as AuthController
    participant AS as AuthServiceImpl
    participant AM as AuthenticationManager<br/>(from AuthenticationConfiguration)
    participant DP as DaoAuthenticationProvider
    participant UDS as UserServiceImpl<br/>(UserDetailsService)
    participant DB as PostgreSQL
    participant JWT as AuthUtil (JJWT)

    C->>AC: POST /api/auth/login {username, password}
    AC->>AS: login(request)
    AS->>AM: authenticate(usernamePasswordToken)
    AM->>DP: authenticate
    DP->>UDS: loadUserByUsername(username)
    UDS->>DB: findByUsername(...)
    DB-->>UDS: User (implements UserDetails)
    UDS-->>DP: User
    DP->>DP: BCrypt.matches(raw, hash)
    DP-->>AM: Authentication (principal = User)
    AM-->>AS: Authentication
    AS->>JWT: generateToken(user)
    JWT-->>AS: signed JWT
    AS-->>AC: AuthResponse{token, userProfile}
    AC-->>C: 200 OK + token
```

### Security components

| Component | Role |
|---|---|
| `SecurityConfig` | Filter chain (stateless), `BCryptPasswordEncoder`, `AuthenticationManager` from `AuthenticationConfiguration` |
| `UserServiceImpl` | Implements `UserDetailsService`; loads a `User` by username |
| `User` | JPA entity implementing `UserDetails` — the authenticated principal |
| `SecurityExpressions` | `@PreAuthorize` target mapping `ProjectRole` → `ProjectPermission` for RBAC |
| `AuthUtil` | Signs/issues JWTs with a configurable secret + expiry |

---

## Domain Model

The schema models a collaborative SaaS editor with billing and AI-usage tracking. Note that **project ownership is expressed through `ProjectMember` roles** (`OWNER` / `EDITOR` / `VIEWER`) rather than a single owner column — enabling true multi-member collaboration.

```mermaid
erDiagram
    USER ||--o{ PROJECT_MEMBER : "is member of"
    PROJECT ||--o{ PROJECT_MEMBER : "has members"
    PROJECT ||--o{ PROJECT_FILE : contains
    USER ||--o{ PROJECT_FILE : "created / updated"
    PROJECT ||--o{ CHAT_SESSION : has
    USER ||--o{ CHAT_SESSION : starts
    CHAT_SESSION ||--o{ CHAT_MESSAGE : contains
    USER ||--o{ CHAT_MESSAGE : authors
    PROJECT ||--|| PREVIEW : "has live"
    USER ||--o{ SUBSCRIPTION : subscribes
    PLAN ||--o{ SUBSCRIPTION : "billed as"
    USER ||--o{ USAGE_LOG : generates
    PROJECT ||--o{ USAGE_LOG : "scoped to"

    USER {
        Long id PK
        string username UK
        string password
        string name
        instant deleted_at "soft delete"
    }
    PROJECT {
        Long id PK
        string name
        bool isPublic
        instant deletedAt "soft delete"
    }
    PROJECT_MEMBER {
        Long projectId PK,FK
        Long userId PK,FK
        enum role "OWNER/EDITOR/VIEWER"
        instant invitedAt
        instant acceptedAt
    }
    PROJECT_FILE {
        Long id PK
        string path
        string minioObjectKey "object storage"
    }
    CHAT_SESSION {
        Long id PK
        string title
    }
    CHAT_MESSAGE {
        Long id PK
        enum role
        string content
        int tokensUsed
    }
    PREVIEW {
        Long id PK
        string namespace "k8s"
        string podName "k8s"
        string previewUrl
        enum status
    }
    PLAN {
        Long id PK
        string name
        int maxProjects
        int maxTokensPerDay
        bool unLimitedAi
    }
    SUBSCRIPTION {
        Long id PK
        enum status
        string stripeSubscriptionId
        instant currentPeriodEnd
    }
    USAGE_LOG {
        Long id PK
        string action
        int tokensUsed
        int durationMs
    }
```

**Modeling techniques on show:** composite primary key via `@EmbeddedId` + `@MapsId` (`ProjectMember`), soft deletes (`deletedAt`), automatic auditing (`@CreationTimestamp` / `@UpdateTimestamp`), query-tuned composite indexes on `projects`, and forward-looking integrations (MinIO object storage for files, Kubernetes for previews, Stripe for billing).

---

## API Reference

Base path: `/api`

### Auth
| Method | Endpoint | Status | Description |
|---|---|---|---|
| POST | `/auth/signup` | ✅ | Register a user, hash password, return JWT + profile |
| POST | `/auth/login` | ✅ | Authenticate credentials, return JWT + profile |
| GET | `/auth/me` | ⬜ | Current user's profile |

### Projects
| Method | Endpoint | Status | Description |
|---|---|---|---|
| GET | `/projects` | ✅ | List projects accessible to the user |
| GET | `/projects/{id}` | ✅ | Access-checked project fetch |
| POST | `/projects` | ✅ | Create project (caller becomes `OWNER`) |
| PATCH | `/projects/{id}` | ✅ | Owner-only rename |
| DELETE | `/projects/{id}` | ✅ | Owner-only soft delete |

### Members
| Method | Endpoint | Status | Description |
|---|---|---|---|
| GET | `/projects/{projectId}/members` | ✅ | Owner + invited members |
| POST | `/projects/{projectId}/members` | ✅ | Owner-only invite (validated) |
| PATCH | `/projects/{projectId}/members/{memberId}` | ⬜ | Change member role |
| DELETE | `/projects/{projectId}/members/{memberId}` | ⬜ | Remove member |

### Files · Billing · Usage
| Method | Endpoint | Status | Description |
|---|---|---|---|
| GET | `/projects/{projectId}/files` | ⬜ | File tree |
| GET | `/projects/{projectId}/files/{*path}` | ⬜ | File content |
| GET | `/plans` | ⬜ | Available plans |
| GET | `/me/subscription` | ⬜ | Current subscription |
| POST | `/stripe/checkout` | ⬜ | Create checkout session |
| POST | `/stripe/portal` | ⬜ | Customer portal link |
| GET | `/usage/today` | ⬜ | Today's usage |
| GET | `/usage/limits` | ⬜ | Plan limits |

**Error contract** — handled centrally by `GlobalExceptionHandler`:

| Exception | HTTP | Body |
|---|---|---|
| `BadRequestException` | 400 | `ApiError` |
| `ResourceNotFoundException` | 404 | `ApiError` |
| `MethodArgumentNotValidException` | 400 | `ApiError` (field errors) |

---

## Configuration & Secrets (12-Factor)

Configuration follows the **commit the config, externalize the secrets** principle.

```mermaid
flowchart TB
    subgraph Git["✅ Committed to Git (no secrets)"]
        base["application.yaml<br/>uses ${ENV:default} placeholders"]
        example["application-local.example.yaml<br/>template to copy"]
    end
    subgraph Local["🔒 Local only (git-ignored)"]
        local["application-local.yaml<br/>real dev secret + DB password"]
    end
    subgraph Prod["🔒 Production"]
        env["Environment variables<br/>/ Secrets Manager (Vault, AWS, …)"]
    end

    base -->|profile: local| local
    base -->|SPRING_PROFILES_ACTIVE=prod| env
```

- `application.yaml` is committed and contains **no secrets** — only `${DB_URL:...}`, `${JWT_SECRET_KEY}`, etc.
- `${JWT_SECRET_KEY}` has **no default**, so a misconfigured production deploy **fails fast** instead of running with a weak key.
- Local development uses the `local` profile (default), which loads the git-ignored `application-local.yaml`.
- Production sets `SPRING_PROFILES_ACTIVE=prod` and injects secrets via the environment.

---

## Getting Started

### Prerequisites
- Java 21+
- PostgreSQL running locally (default: `localhost:5432`, database `saas_ai`)

### 1. Provide local config
```bash
cp src/main/resources/application-local.example.yaml \
   src/main/resources/application-local.yaml
# edit application-local.yaml: set your DB password (if any) and a dev JWT secret
```

### 2. Run
```bash
./mvnw spring-boot:run         # starts on the 'local' profile by default
```

### 3. Try it
```bash
# Sign up
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"vikki","password":"secret123","name":"Vikki"}'

# Log in
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"vikki","password":"secret123"}'
```

### Build & test
```bash
./mvnw clean compile
./mvnw test
```

---

## Project Structure

```
src/main/java/com/viki/projects/saas_ai_editor
├── controller/      # REST endpoints
├── service/         # interfaces
│   └── impl/        # business logic
├── repository/      # Spring Data JPA
├── mapper/          # MapStruct
├── dto/             # API contracts (records): auth · project · member · subscription
├── entity/          # JPA models
├── enums/           # domain enums (ProjectRole, ProjectPermission, …)
├── security/        # JWT, SecurityConfig, SecurityExpressions (RBAC)
└── error/           # ApiError + global handler
```

---

## Roadmap

- [x] JWT authentication **filter** to populate `SecurityContext` and protect `/api/**`
- [x] Method-level RBAC (`@PreAuthorize`) on project view/edit/delete
- [x] Extend RBAC to member-management (`VIEW_MEMBERS` / `MANAGE_MEMBERS`)
- [ ] Extend RBAC to file/billing/usage endpoints
- [ ] `GET /auth/me` and member role-update / removal logic
- [ ] File service + MinIO object storage integration
- [ ] Stripe billing: checkout, customer portal, subscription sync
- [ ] Usage tracking + quota enforcement against `Plan` limits
- [ ] Live previews via Kubernetes
- [ ] Integration tests on an isolated `test` profile (Testcontainers)

---

<sub>Built as a portfolio project to demonstrate production-style Spring Boot backend engineering.</sub>
