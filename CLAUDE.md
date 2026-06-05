# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

Each service is a standalone Maven project. From any service directory:

```bash
mvn clean install          # Build and run tests
mvn clean install -DskipTests
mvn spring-boot:run        # Run locally
```

Build and push Docker images (uses Google Jib, runs during `install`):
```bash
mvn install                # Builds image and pushes to yogindersingh634/ on DockerHub
```

Docker Compose (recommended for full-stack local development):
```bash
docker compose -f docker-files/default/docker-compose.yaml up -d
docker compose -f docker-files/default/docker-compose.yaml down
```

Kubernetes:
```bash
kubectl apply -f kubernetes-yaml/   # Deploy in order (files are numbered 1–8)
```

## Architecture Overview

Seven services organized into infrastructure and business layers:

**Infrastructure (start these first):**
- `spring-cloud-config-server` (8071) — reads properties from a remote Git repo (`yogindersingh/config-properties`)
- `spring-eureka-server` (8070) — service registry; all services self-register
- `spring-cloud-gateway-server` (8072) — reactive API Gateway; entry point for all external traffic

**Business services:**
- `Accounts` (8080) — orchestrates the other two via Feign; owns the `/bank/accounts/**` routes
- `Cards` (9001) — `/bank/cards/**`
- `Loans` (8090) — `/bank/loans/**`
- `message` (9011) — headless async worker; no HTTP routes

## Inter-Service Communication

**Synchronous (Feign):** `Accounts` calls `Cards` and `Loans` using Spring Cloud OpenFeign with Resilience4j circuit breakers. Circuit breakers trip at 50% failure rate over 10 calls.

**Asynchronous (RabbitMQ / Spring Cloud Stream):**
- `Accounts` publishes events to the `email-sms` exchange after account operations.
- `message` service binds `email` and `sms` functions to consume from `email-sms` and responds on `accounts-connection`.
- `Accounts` has a consumer on `accounts-connection` to receive those responses.

**Correlation tracing:** Every request carries an `X-Correlation-ID` header injected/forwarded by the Gateway filter. Logs include `trace_id` and `span_id` via OpenTelemetry Java agent (`opentelemetry-javaagent-2.22.0.jar`).

## Gateway Routing & Resilience Policies

| Route prefix | Target | Policy |
|---|---|---|
| `/bank/accounts/**` | `lb://ACCOUNTS` | Circuit breaker → `/contactsupport` fallback |
| `/bank/cards/**` | `lb://CARDS` | Retry: 3 attempts, 100ms–1s exponential backoff |
| `/bank/loans/**` | `lb://LOANS` | Redis rate limiter: 1 req/sec |

## Security

OAuth2/JWT via Keycloak running at port 7080 (realm: `master`). The Gateway is the OAuth2 resource server; downstream services trust the forwarded JWT. JWK Set URI: `http://keycloak:7080/realms/master/protocol/openid-connect/certs`.

## Configuration Management

All runtime properties live in the external Git repo consumed by the Config Server. Local `application.yaml` files hold only bootstrap values (config server URL, port, spring profile). To change a service's configuration, update that Git repo and either restart or trigger a bus refresh via the RabbitMQ event bus (`spring-cloud-bus-amqp`).

Active profiles per service: `Accounts=prod`, `Cards=qa`, `Loans=default`.

## Observability Stack (Docker Compose only)

| Tool | Port | Purpose |
|---|---|---|
| Prometheus | 9090 | Scrapes `/actuator/prometheus` on all services |
| Grafana | 3000 | Dashboards; datasources in `docker-files/observability/datasource/` |
| Tempo | 3110 | Distributed traces (OTLP endpoint `4318`) |
| Loki | 3100 | Log aggregation (backed by MinIO) |
| Alloy | — | OpenTelemetry collector |

## Key Infrastructure Credentials (local/dev only)

| Service | Credential |
|---|---|
| MySQL | root / root@1234 (port 3307 mapped externally) |
| Redis | password: `password` |
| RabbitMQ management UI | 15672 |

## Stack Versions

- Spring Boot: 4.0.5 (business services), 4.0.6 (infrastructure services)
- Spring Cloud: 2025.1.1
- Java: 17 (business services), 21 (infrastructure services)
- Docker image tag: `0.0.4-SNAPSHOT`
