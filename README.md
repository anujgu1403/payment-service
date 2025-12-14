
# Catalog Service (Spring Boot + JPA + PostgreSQL)

## What's included
- Spring Boot 3.3.x (Java 21)
- JPA with PostgreSQL driver
- Dockerfile
- Kubernetes manifests (Postgres + Catalog service)
- OpenAPI (springdoc) configuration
- Unit tests for controllers (JUnit 5 + MockMvc)

## How to run locally with Postgres
1. Start a postgres instance (docker):
   ```bash
   docker run -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=catalogdb -p 5432:5432 -d postgres:15
   ```
2. Build and run:
   ```bash
   ./gradlew bootRun
   ```

## Docker
Build the image:
```bash
./gradlew bootJar
docker build -t catalog-service:latest .
```

## Kubernetes
Apply manifests:
```bash
kubectl apply -f k8s/postgres-deployment.yaml
kubectl apply -f k8s/catalog-deployment.yaml
```

## Swagger
http://localhost:8083/v3/api-docs
http://localhost:8083/swagger-ui/index.html