---
applyTo: "infra/**"
---

# Agent Instructions for CommonEx Infrastructure

## Project Overview

CommonEx infrastructure uses Docker and Docker Compose for containerization, Nginx as a reverse
proxy with OpenTelemetry support, and OpenTelemetry Collector for observability. The setup supports
blue-green deployment for backend services.

## Technology Stack

- **Containerization**: Docker
- **Reverse Proxy**: Nginx (with OpenTelemetry module)
- **Observability**: OpenTelemetry Collector
- **Orchestration**: Docker Compose for production and development

## Architecture

- Multi-container setup with Docker Compose
- Nginx as reverse proxy and gateway
- OpenTelemetry for distributed tracing and metrics
- Blue-green deployment support for backend services

## Components

### Nginx

- Custom build with OpenTelemetry module (`ngx_otel_module`)
- HTTP/2 and HTTP/3 (QUIC) support
- Brotli compression
- SSL/TLS termination
- Upstream load balancing for backend services

### OpenTelemetry Collector

- Receives traces and metrics from services
- Exports to configured backends
- Configured via `otel-collector-config.yaml`

### Docker Compose

- **Production**: `docker-compose-prod.yml` - production deployment with blue-green backend

## Prerequisites

- **Docker 24+** (check with `docker --version`)
- **Docker Compose 2+** (check with `docker compose version`)
- **Git** for version control

## Environment Setup

### Docker Installation

Ensure Docker and Docker Compose are installed and running:

```bash
# Check Docker
docker --version

# Check Docker Compose
docker compose version

# Verify Docker daemon is running
docker ps
```

### Configuration

- Environment variables configured on the server
- Docker network setup for service communication
- Volume mounts for persistent data (if needed)

## Essential Commands

**Run commands from repository root or `infra/` directory as specified.**

### Starting Services

```bash
# Production (from repo root)
docker compose -f infra/docker-compose-prod.yml up -d --pull always

# Production (with build)
docker compose -f infra/docker-compose-prod.yml up -d --build

# View logs while starting
docker compose -f infra/docker-compose-prod.yml up
```

### Stopping Services

```bash
# Stop services (from repo root)
docker compose -f infra/docker-compose-prod.yml down

# Stop and remove volumes
docker compose -f infra/docker-compose-prod.yml down -v
```

### Viewing Logs

```bash
# All services
docker compose -f infra/docker-compose-prod.yml logs -f

# Specific service
docker compose -f infra/docker-compose-prod.yml logs -f nginx
docker compose -f infra/docker-compose-prod.yml logs -f backend
docker compose -f infra/docker-compose-prod.yml logs -f otel-collector

# Last 100 lines
docker compose -f infra/docker-compose-prod.yml logs --tail=100
```

### Service Management

```bash
# Restart a specific service
docker compose -f infra/docker-compose-prod.yml restart nginx

# Scale services (if applicable)
docker compose -f infra/docker-compose-prod.yml up -d --scale backend=2

# Check service status
docker compose -f infra/docker-compose-prod.yml ps
```

## Development Workflow

### Production Deployment

1. Use `docker-compose-prod.yml` for production
2. Configure environment variables on the server
3. Deploy via CI/CD pipeline or manually:
   ```bash
   docker compose -f infra/docker-compose-prod.yml up -d --pull always
   ```

### Local Development

1. Use development compose file if available
2. Configure local environment variables
3. Start services for local testing

## Configuration

### Nginx Configuration

- **Production config**: `infra/nginx/nginx-prod.conf`
- **Custom Dockerfile**: `infra/nginx/Dockerfile`
- Includes OpenTelemetry module configuration
- Upstream configuration for backend services (blue-green)

### OpenTelemetry Configuration

- **Config file**: `infra/otel-collector/otel-collector-config.yaml`
- **Dockerfile**: `infra/otel-collector/Dockerfile`
- **Manifest**: `infra/otel-collector/manifest.yaml`

### Docker Compose Files

- **Production**: `infra/docker-compose-prod.yml`

## Deployment

### Health Checks

- Services include health check configurations
- Nginx monitors upstream health
- Backend services expose `/health` endpoints

### Blue-Green Deployment

- Backend services support blue-green deployment
- Nginx configured for upstream load balancing
- Zero-downtime deployments possible

## Common Tasks

### Rebuilding Services

```bash
# Rebuild and restart all services
docker compose -f infra/docker-compose-prod.yml up -d --build

# Rebuild specific service
docker compose -f infra/docker-compose-prod.yml build nginx
docker compose -f infra/docker-compose-prod.yml up -d nginx
```

### Updating Configuration

1. Update configuration files
2. Rebuild affected services: `docker compose -f infra/docker-compose-prod.yml build [service]`
3. Restart services: `docker compose -f infra/docker-compose-prod.yml up -d [service]`

### Inspecting Services

```bash
# Execute command in running container
docker compose -f infra/docker-compose-prod.yml exec nginx sh
docker compose -f infra/docker-compose-prod.yml exec backend sh

# Check container resource usage
docker stats

# Inspect container configuration
docker compose -f infra/docker-compose-prod.yml config
```

## Validation Steps

Before deploying, verify:

```bash
# 1. Check Docker Compose configuration is valid
docker compose -f infra/docker-compose-prod.yml config

# 2. Check service status
docker compose -f infra/docker-compose-prod.yml ps

# 3. Check service health
docker compose -f infra/docker-compose-prod.yml ps --format json | jq '.[] | {name: .Name, status: .State}'

# 4. Verify network connectivity
docker compose -f infra/docker-compose-prod.yml exec backend ping nginx
```

## Troubleshooting

### Container Issues

- **Container won't start**: Check logs:
  `docker compose -f infra/docker-compose-prod.yml logs [service-name]`
- **Container keeps restarting**: Check exit codes and logs
- **Port conflicts**: Verify ports aren't already in use: `netstat -tuln` or `lsof -i :PORT`
- **Out of memory**: Check Docker resource limits and system memory

### Network Issues

- **Services can't communicate**: Verify Docker network exists: `docker network ls`
- **Connection refused**: Check service is running and listening on correct port
- **DNS resolution**: Verify service names match in docker-compose.yml

### Logging Issues

- **No logs appearing**: Check log driver configuration
- **Logs too verbose**: Adjust log levels in service configuration
- **View Nginx error logs**:
  `docker compose -f infra/docker-compose-prod.yml exec nginx cat /var/log/nginx/error.log`

### OpenTelemetry Issues

- **No traces appearing**: Check collector configuration and service connectivity
- **Collector not receiving data**: Verify service instrumentation and collector endpoints
- **Review collector logs**: `docker compose -f infra/docker-compose-prod.yml logs otel-collector`

### Common Errors

- **"Cannot connect to Docker daemon"**: Ensure Docker daemon is running
- **"Port already allocated"**: Change port mapping or stop conflicting service
- **"No such service"**: Verify service name in docker-compose.yml
- **"Permission denied"**: Check Docker socket permissions or use `sudo` (not recommended)
- **"Network not found"**: Recreate network: `docker compose -f infra/docker-compose-prod.yml down`
  then `up`

### Resource Issues

- **Docker resource limits**: Check Docker Desktop settings or daemon configuration
- **Disk space**: Clean up unused images: `docker system prune -a`
- **Memory**: Monitor with `docker stats` and adjust limits if needed
