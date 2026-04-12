# Quick Guide to Apicurio Registry (with Camel Reference)

Apicurio Registry is a runtime registry for API artifacts and schemas. It allows you to decouple your data formats from your applications by managing them in a central location.

## 1. Key Concepts

-   **Artifact**: A single schema or API definition (e.g., JSON Schema, Avro, Protobuf, OpenAPI).
-   **Group**: A logical collection of artifacts (default is `default`).
-   **Version**: Every change to an artifact creates a new version, ensuring compatibility.
-   **SerDes (Serializer/Deserializer)**: Libraries used by Kafka clients to automatically fetch/register schemas from the registry.

## 2. Integration with Our Camel Project

In the `camel-apicuro` project, we use the **JSON Schema Kafka SerDes** to validate messages for all three event types: books, orders, and shipments.

### How it works:
1.  **Producer Side**: When a route sends a message (e.g., `produce-book-added`), the `JsonSchemaKafkaSerializer` looks at the data.
2.  **Auto-Registration**: Because `apicurio.registry.auto-register=true` is set in the URI, the serializer automatically uploads the schema found in the message (or derived from it) to the registry if it's missing.
3.  **Artifact ID**: By default, it creates artifacts named `[topic]-value` (e.g., `bookstore-books-value`, `bookstore-orders-value`).
4.  **Consumer Side**: The `JsonSchemaKafkaDeserializer` reads the "Global ID" from the Kafka message header, fetches the matching schema from Apicurio, and validates the incoming JSON.

## 3. Useful API Operations

Once your infrastructure is up (using `./start.sh` or `podman-compose up -d`) and the Camel application has been run, you can interact with Apicurio via `curl`:

### Check System Info
```bash
curl http://localhost:8080/api/system/info
```

### List all Artifacts
```bash
curl http://localhost:8080/api/artifacts
```

### View a Specific Schema (after running the Camel app)
```bash
# List all artifacts first
curl http://localhost:8080/api/artifacts

# View a specific artifact (replace with actual artifact ID)
curl http://localhost:8080/api/artifacts/{artifact-id}
```

### Manually Register a Schema (if needed)
If the Camel application hasn't been run yet, you can manually register schemas:
```bash
curl -X POST http://localhost:8080/api/artifacts \
  -H "Content-Type: application/json" \
  -H "X-Registry-ArtifactType: JSON" \
  -d @src/main/resources/schemas/bookstore-event.json
```

## 4. The Web UI
Apicurio provides a user-friendly interface to manage your schemas visually.
- **URL**: [http://localhost:8080/ui](http://localhost:8080/ui)

## 5. Alternative Kafka Web UI (Kafbat)
For a more comprehensive Kafka management experience, you can use **Kafbat**:
- **URL**: [http://localhost:8085](http://localhost:8085)
- **Features**: Browse topics, view messages, manage consumer groups, inspect broker metrics
- **Start with**: `podman-compose up -d kafbat`

## 6. Why Use This?
-   **Validation**: Catch "bad" data at the producer level before it pollutes your Kafka topics.
-   **Evolution**: Update your JSON schema in `src/main/resources/schemas/` and Apicurio will manage the versioning for you.
-   **Consistency**: Ensure all consumers of your Kafka topics are using the same data contract.

## 6. Available Schemas

The bookstore application uses these JSON schemas:

| Topic | Schema File | Description |
|-------|-------------|-------------|
| `bookstore-books` | `src/main/resources/schemas/bookstore-event.json` | BookAddedEvent, OrderCreatedEvent, ShipmentCreatedEvent |
| `bookstore-orders` | Same as above | Order events with nested items |
| `bookstore-shipments` | Same as above | Shipment tracking events |

**Note**: Schemas are auto-registered when the Camel application produces messages to Kafka. If you haven't run the application yet, manually register the schema using the command above.

## 7. Troubleshooting

### Kafbat Not Connecting
If Kafbat cannot connect to Kafka:
1. Ensure Kafka is running: `podman ps | grep kafka`
2. Check logs: `podman logs kafbat`
3. Verify Kafka advertised listeners match your configuration
