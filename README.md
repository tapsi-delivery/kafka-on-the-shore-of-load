# Kafka Load Testing and Monitoring Project

This project is a Spring Boot application written in Kotlin, which uses Kafka for messaging and MongoDB for data persistence. It is configured for monitoring with Prometheus and Kafka-Node-Exporter, making it suitable for running load tests on Kafka.

## Prerequisites

- Java 11 or higher
- Docker and Docker Compose
- Gradle

## Getting Started

1. Clone the repository:
```bash
git clone git@github.com:tapsi-pack/kafka-produce-consumer-example.git
cd your-repo-name
```

2. Build the project:
```bash
./gradlew clean build
```

3. Start the services (Kafka, MongoDB, Prometheus, Kafka-Node-Exporter) using Docker Compose:
```bash
docker-compose up -d
```

4. Run the application:
```bash
./gradlew bootRun
```

## Monitoring

The application is configured to expose metrics at the `/metrics` endpoint for Prometheus to scrape. You can access the Prometheus dashboard at `http://localhost:9090`.

Kafka-Node-Exporter is also set up for Kafka metrics, which can be accessed at `http://localhost:9308/metrics`.

## Load Testing

You can perform load testing on Kafka using your preferred load testing tool. Ensure to target the Kafka broker running at `localhost:9092`.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.