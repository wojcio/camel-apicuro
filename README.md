# Camel XML DSL and Apicurio Registry Setup

This project demonstrates how to set up an Apache Camel application using XML DSL that integrates with **Apicurio Registry** for schema management (specifically for Kafka serialization/deserialization with **JSON Schema**).

## Features
- **Camel XML DSL**: Routes are defined in `src/main/resources/camel/routes.xml`.
- **JSON Schema Validation**: Uses Apicurio's JSON Schema Kafka SerDes to validate messages against a schema (`src/main/resources/schemas/user-event.json`).
- **Error Handling**: Demonstrates catching validation errors when producing malformed data.
- **Configurable**: Environment-specific settings are in `src/main/resources/application.properties`.

## Prerequisites
- Java 17+
- Maven
- Podman (or Docker) and Podman Compose

## Infrastructure Setup
To start Kafka and Apicurio Registry using Podman:
```bash
podman-compose up -d
```
This will start:
- **Kafka** (KRaft mode) on `localhost:9092`
- **Apicurio Registry** (In-memory) on `http://localhost:8080`

## Running the Project
Once the infrastructure is up, run the Camel application:
```bash
mvn camel:run
```
