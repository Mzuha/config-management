# Configuration Microservices Project

## Project Structure

*   `config-service`: The core service managing configurations.
*   `config-client`: A consumer microservice that receives updates and interacts with the `config-service` through Kafka topics.
*   `docker-compose.yml`: Orchestrates the entire infrastructure.

## Getting Started

### Prerequisites
*   Docker and Docker Compose

### Quick Start (Docker Compose)
Run the following command to start the project:

```
docker-compose up -d --build
```

### Documentation
After launching the project, documentation is available at http://localhost:8081/swagger-ui.html.

### URLS
*   `http://localhost:8081/` - Configuration Service
*   `http://localhost:8082/` - Configuration Client
*   `http://localhost:8080/` - Kafka UI
*   `http://localhost:5432/` - DB
