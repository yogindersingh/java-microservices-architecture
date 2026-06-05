# Java Microservices Architecture

A production-ready microservices reference implementation built with Spring Boot 4 and Spring Cloud 2025.1.1, demonstrating service discovery, centralized configuration, API gateway, resilience patterns, async messaging, OAuth2 security, and full observability.

---

## Services

| Service | Port | Description |
|---|---|---|
| `spring-cloud-config-server` | 8071 | Centralized config from external Git repo |
| `spring-eureka-server` | 8070 | Service discovery and registration |
| `spring-cloud-gateway-server` | 8072 | API Gateway — single entry point for clients |
| `Accounts` | 8080 | Account management; orchestrates Cards and Loans |
| `Cards` | 9001 | Credit card management |
| `Loans` | 8090 | Loan management |
| `message` | 9011 | Async notification worker (RabbitMQ) |

---

## Architecture

```
Client
  └─► API Gateway (8072)
        ├─ OAuth2/JWT validation (Keycloak)
        ├─ Circuit breaker, retry, rate limiting per route
        └─► Service Discovery (Eureka 8070)
              ├─► Accounts (8080) ──Feign──► Cards (9001)
              │                    └──Feign──► Loans (8090)
              └─► Config Server (8071) ◄─── Git repo (config-properties)

RabbitMQ
  ├─ Accounts publishes ──► [email-sms] ──► Message service
  └─ Message service replies ──► [accounts-connection] ──► Accounts
```

**Synchronous:** Accounts calls Cards and Loans via OpenFeign with Resilience4j circuit breakers (trip at 50% failure over 10 calls).

**Asynchronous:** Spring Cloud Stream over RabbitMQ — Accounts publishes account events; the Message service processes them and replies on a return queue.

**Routing policies:**

| Route | Target | Resilience |
|---|---|---|
| `/bank/accounts/**` | `lb://ACCOUNTS` | Circuit breaker → `/contactsupport` fallback |
| `/bank/cards/**` | `lb://CARDS` | Retry: 3 attempts, 100ms–1s exponential backoff |
| `/bank/loans/**` | `lb://LOANS` | Redis rate limiter: 1 req/sec |

---

## Stack

- **Java:** 17 (business services), 21 (infrastructure services)
- **Spring Boot:** 4.0.5 / 4.0.6
- **Spring Cloud:** 2025.1.1
- **Security:** OAuth2 + JWT via Keycloak (port 7080, realm `master`)
- **Messaging:** RabbitMQ via Spring Cloud Stream
- **Tracing:** OpenTelemetry Java agent → Tempo
- **Metrics:** Micrometer → Prometheus → Grafana
- **Logs:** Loki + Alloy
- **Rate limiting:** Redis
- **Build/image:** Maven + Google Jib → DockerHub (`yogindersingh634/`), image tag `0.0.4-SNAPSHOT`

---

## Commands

### Build

```bash
# Build a single service
cd Accounts && mvn clean install

# Build and skip tests
mvn clean install -DskipTests

# Build Docker image via Jib (pushed to DockerHub)
mvn compile jib:dockerBuild

# Build Docker image via Buildpacks
mvn spring-boot:build-image

# Build Docker image via Dockerfile
docker build . -t <registry>/<image>:<version>
```

### Run locally

```bash
# Run a single service
mvn spring-boot:run
```

### Docker Compose (full stack)

Start infrastructure in order: RabbitMQ and MySQL come up first via healthcheck dependencies defined in `common-config.yaml`.

```bash
docker compose -f docker-files/default/docker-compose.yaml up -d
docker compose -f docker-files/default/docker-compose.yaml down
docker compose -f docker-files/default/docker-compose.yaml config   # validate
```

### Kubernetes

Files are numbered to reflect deployment order (Keycloak → ConfigMaps → Config Server → Eureka → services → Gateway).

```bash
kubectl apply -f kubernetes-yaml/          # deploy all
kubectl apply -f kubernetes-yaml/5_accounts.yml   # deploy one service
```

### Helm

A single umbrella chart (`helm/`) covers all services and infrastructure. Each component (accounts, cards, loans, gateway, config-server, eureka-server, message, keycloak, mysql, rabbitmq, redis) has its own sub-template under `helm/templates/`. Toggle components on/off and override images or resource limits via `helm/values.yaml`.

```bash
# Install (from repo root)
helm install java-microservices ./helm

# Upgrade
helm upgrade java-microservices ./helm

# Override a value inline
helm upgrade java-microservices ./helm --set accounts.image.tag=0.0.5-SNAPSHOT

# Dry-run / template preview
helm template java-microservices ./helm
```

Current image tag across services: `0.0.4-SNAPSHOT` (DockerHub: `yogindersingh634/<service>`).

> **Note:** The Gateway and Keycloak services are exposed via `NodePort` (30072 and 30780 respectively). All other services use `ClusterIP`.

---

## Observability (Docker Compose)

| Tool | URL | Purpose |
|---|---|---|
| Grafana | http://localhost:3000 | Dashboards |
| Prometheus | http://localhost:9090 | Metrics (scrapes `/actuator/prometheus`) |
| Tempo | http://localhost:3110 | Distributed traces |
| Loki | http://localhost:3100 | Log aggregation |
| RabbitMQ UI | http://localhost:15672 | Message broker management |
| Keycloak | http://localhost:7080 | Identity provider |

Distributed tracing uses the OpenTelemetry Java agent (`opentelemetry-javaagent-2.22.0.jar`). Every request carries an `X-Correlation-ID` header injected by the Gateway and forwarded to downstream services.

---

## Configuration

Runtime properties are stored in an external Git repository consumed by the Config Server. Local `application.yaml` files contain only bootstrap values (config server URL, port, active profile). To propagate config changes without restarting, trigger a bus refresh via the RabbitMQ event bus (`spring-cloud-bus-amqp`).

Active profiles: `Accounts=prod`, `Cards=qa`, `Loans=default`.

---

## Local Dev Credentials

| Service | Details |
|---|---|
| MySQL | `root` / `root@1234`, mapped to port `3307` |
| Redis | password: `password` |
| RabbitMQ | default guest credentials, management UI on `15672` |
