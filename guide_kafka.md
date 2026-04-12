# Quick Guide to Apache Kafka (with Camel Reference)

Apache Kafka is a distributed event streaming platform. In our project, it serves as the messaging backbone between producers and consumers.

## 1. Key Concepts

-   **Topic**: A category or feed name to which records are published (e.g., `bookstore-orders`).
-   **Broker**: A Kafka server that stores data and serves clients. Our setup uses a single broker.
-   **Producer**: An application that sends data to Kafka topics (e.g., our `produce-book-added` route).
-   **Consumer**: An application that reads data from Kafka topics (e.g., our `consume-and-report` route).
-   **KRaft Mode**: A modern Kafka architecture that removes the dependency on Apache ZooKeeper by using an internal Raft-based consensus protocol.

## 2. Integration with Our Camel Project

We use the `camel-kafka` component to interact with the broker.

### Configuration in `application.properties`:
```properties
kafka.brokers = localhost:9092
kafka.topic.books = bookstore-books
kafka.topic.orders = bookstore-orders
kafka.topic.shipments = bookstore-shipments
```

### URI Anatomy in `routes.xml`:
```xml
<to uri="kafka:{{kafka.topic}}?brokers={{kafka.brokers}}&amp;valueSerializer=..."/>
```
- **`brokers`**: Tells Camel where the Kafka cluster is.
- **`valueSerializer` / `valueDeserializer`**: Plugs in the Apicurio Registry logic to handle the data format automatically.

## 3. Kafka Topics

The bookstore application uses these topics:

| Topic | Purpose |
|-------|---------|
| `bookstore-books` | New books added to catalog |
| `bookstore-orders` | Customer orders |
| `bookstore-shipments` | Shipment notifications |
| `bookstore-dlq` | Dead Letter Queue for failed messages |
| `bookstore-expedited` | Expedited shipments (filtered) |

## 4. Useful CLI Operations (via Podman)

Since Kafka is running in a container named `kafka`, you can use `podman exec` to run internal scripts:

### List Topics
```bash
podman exec -it kafka /bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

### Describe a Topic
```bash
podman exec -it kafka /bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic bookstore-orders --describe
```

### Consume Messages Manually (Console Consumer)
```bash
podman exec -it kafka /bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic bookstore-orders --from-beginning
```
*Note: Since we use binary SerDes (Apicurio), the console consumer might show some non-printable characters in the headers.*

### Create a New Topic
```bash
podman exec -it kafka /bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --create --topic my-topic \
  --partitions 1 --replication-factor 1
```

## 5. Web UI for Kafka Management (Kafbat)

For a visual interface to manage and monitor Kafka, use **Kafbat**:

- **URL**: [http://localhost:8085](http://localhost:8085)
- **Features**:
  - Browse Kafka topics
  - View messages in real-time
  - Manage consumer groups
  - Inspect broker metrics and lag

### Start Kafbat
```bash
podman-compose up -d kafbat
```

Or restart all services:
```bash
./start.sh
```

## 6. Why Use Kafka?
- **Scalability**: Can handle millions of events per second.
- **Persistence**: Messages are stored on disk and can be replayed.
- **Decoupling**: Producers don't need to know who the consumers are.

## 6. Troubleshooting
- If Camel cannot connect, ensure the container is running: `podman ps`.
- Check logs: `podman logs kafka`.
- Ensure the advertised listeners in `compose.yaml` match your host access (currently set to `localhost:9092`).
- If Kafbat cannot connect, ensure Kafka is running and accessible on port 9092.
