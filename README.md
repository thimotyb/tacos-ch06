# Taco Cloud – Spring Boot 3 Upgrade

This project modernises the Chapter 6 Taco Cloud sample from *Spring in Action*
for Java 17, Spring Boot 3 / Spring Framework 6, and introduces a lightweight
React UI.

## Purpose

- Demonstrate a multi-module Spring Boot 3 application (domain, data, security,
  REST API, legacy MVC).
- Showcase Spring Security 6, Spring HATEOAS, and Jakarta APIs in action.
- Provide a minimal React UI for interacting with the REST services.

## Modules

| Module                | Description |
|----------------------|-------------|
| `tacocloud-domain`   | Jakarta Persistence entities & validation rules |
| `tacocloud-data`     | Spring Data repositories |
| `tacocloud-security` | Spring Security configuration (filter chain, custom user details) |
| `tacocloud-api`      | REST endpoints + Spring HATEOAS resources |
| `tacocloud-web`      | Legacy Thymeleaf MVC module (reference only) |
| `tacos`              | Aggregated Spring Boot application |
| `tacocloud-ui-react` | Standalone Vite + React frontend (not part of Maven reactor) |

## Prerequisites

- Java 17 (e.g. Temurin 17.0.12)
- Maven Wrapper (`./mvnw`)
- Node.js 18+ for the React UI

## Build Backend

```bash
./mvnw clean package
```

## Run Backend

```bash
java -jar tacos/target/taco-cloud-0.0.6-SNAPSHOT.jar
```

- REST API: `http://localhost:8080/api`
- Legacy MVC UI: `http://localhost:8080`

## Run React UI

```bash
cd tacocloud-ui-react
npm install
npm run dev
```

The development server listens on `http://localhost:5173` and proxies `/api`
requests to the backend (configurable via `VITE_API_BASE_URL`).

## Refactor Highlights

- Spring Boot 3 / Spring Framework 6 upgrade (Jakarta namespace changes).
- Spring Security 6 filter-chain configuration (`SecurityFilterChain`,
  `AuthenticationManager`).
- Modern Spring HATEOAS (`RepresentationModel`, `EntityModel`, lambda processors).
- Lombok removed in favour of explicit Java accessors and constructors.
- New React client keeps the UI lightweight while consuming the REST API.

See `REFACTOR.md` for the full migration log and `SECURITY_NOTES.md` for a deeper
dive into the Spring Security pieces.
