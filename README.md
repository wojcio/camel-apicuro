# Camel XML DSL and Apicurio Registry Setup

This project demonstrates how to set up an Apache Camel application using XML DSL that integrates with **Apicurio Registry** for schema management (specifically for Kafka serialization/deserialization with Avro).

## Features
- **Camel XML DSL**: Routes are defined in `src/main/resources/camel/routes.xml`.
- **Apicurio Integration**: Uses Apicurio's Avro Kafka SerDes to interact with a schema registry.
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
