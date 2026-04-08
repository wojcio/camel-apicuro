# Quick Guide to Apicurio Registry (with Camel Reference)

Apicurio Registry is a runtime registry for API artifacts and schemas. It allows you to decouple your data formats from your applications by managing them in a central location.

## 1. Key Concepts

-   **Artifact**: A single schema or API definition (e.g., JSON Schema, Avro, Protobuf, OpenAPI).
-   **Group**: A logical collection of artifacts (default is `default`).
-   **Version**: Every change to an artifact creates a new version, ensuring compatibility.
-   **SerDes (Serializer/Deserializer)**: Libraries used by Kafka clients to automatically fetch/register schemas from the registry.

## 2. Integration with Our Camel Project

In the `camel-apicuro` project, we use the **JSON Schema Kafka SerDes**.

### How it works:
1.  **Producer Side**: When the `produce-valid-event` route sends a message, the `JsonSchemaKafkaSerializer` looks at the data.
2.  **Auto-Registration**: Because `apicurio.registry.auto-register=true` is set in the URI, the serializer automatically uploads the schema found in the message (or derived from it) to the registry if it's missing.
3.  **Artifact ID**: By default, it creates an artifact named `user-events-value` (following the `[topic]-value` convention).
4.  **Consumer Side**: The `JsonSchemaKafkaDeserializer` reads the "Global ID" from the Kafka message header, fetches the matching schema from Apicurio, and validates the incoming JSON.

## 3. Useful API Operations

Once your infrastructure is up (`podman-compose up -d`), you can interact with Apicurio via `curl`:

### Check System Info
```bash
curl http://localhost:8080/apis/registry/v2/system/info
```

### List all Artifacts
```bash
curl http://localhost:8080/apis/registry/v2/search/artifacts
```

### View a Specific Schema (after running the Camel app)
```bash
curl http://localhost:8080/apis/registry/v2/groups/default/artifacts/user-events-value
```

## 4. The Web UI
Apicurio provides a user-friendly interface to manage your schemas visually.
- **URL**: [http://localhost:8080/ui](http://localhost:8080/ui)

## 5. Why Use This?
-   **Validation**: Catch "bad" data at the producer level before it pollutes your Kafka topics.
-   **Evolution**: Update your JSON schema in `src/main/resources/schemas/` and Apicurio will manage the versioning for you.
-   **Consistency**: Ensure all consumers of the `user-events` topic are using the same data contract.
