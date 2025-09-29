# Taco Cloud v0.0.6

This repository contains the multi-module Taco Cloud sample from *Spring in Action*, 5th edition, Chapter 6. The code illustrates how the application evolves into a distributed layout with a REST API and an Angular client.

## Project Structure

The Maven reactor is defined in `pom.xml` and assembles the following modules:

- `tacocloud-domain`: Shared domain model (entities, DTOs, validation).
- `tacocloud-data`: Persistence layer built on Spring Data.
- `tacocloud-security`: Security configuration (currently a work in progress).
- `tacocloud-api`: REST controllers exposing the Taco Cloud API.
- `tacocloud-ui`: Angular client that consumes the REST API.
- `tacocloud-web`: Legacy Spring MVC module retained for reference and cleanup.
- `tacos`: Spring Boot application that bundles the previous modules into an executable JAR.

## Prerequisites

- Java 8 (SDKMAN! identifier `8.0.462-tem` works well under WSL).
- Maven wrapper (included) — no separate Maven installation required.
- Node.js and Angular CLI for rebuilding the UI (`./mvnw` bootstraps Node 6.9.1 automatically if needed).

## Build

From the repository root run:

```bash
./mvnw clean package
```

The build orchestrates backend modules and compiles the Angular frontend via the frontend-maven-plugin. Expect the first build to download Maven, Node, and Angular dependencies.

## Run

After a successful build, start the aggregated Spring Boot app:

```bash
java -jar tacos/target/taco-cloud-0.0.6-SNAPSHOT.jar
```

Navigate to <http://localhost:8080> to explore the Taco Cloud UI. The primary REST endpoints are exposed under `/api`.

## Current Status

- Core domain, data, and REST API modules are functional for Chapter 6 scenarios.
- UI exposes recent taco designs and ordering workflow, though some pages remain placeholders.
- Security features still need refinement (login/registration flows are disabled).

Refer to the original `README.adoc` for historical context from the book.
