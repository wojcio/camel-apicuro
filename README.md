# Bookstore Event Streaming with Apache Camel

This project demonstrates a **Bookstore Event Streaming** system using Apache Camel XML DSL, integrated with **Apicurio Registry** for schema management and **SQLite** for persistent data storage.

## Features

- **Camel XML DSL**: Routes are defined in `src/main/resources/camel/routes.xml`.
- **JSON Schema Validation**: Uses Apicurio's JSON Schema Kafka SerDes to validate messages.
- **SQLite Database**: Persistent book catalog with sample data (`src/main/resources/db/`).
- **Apache Kafka Integration**: Event streaming for books, orders, and shipments.
- **Enterprise Integration Patterns (EIP)**:
  - Split-Aggregate for processing order items
  - Filter for event type routing
  - Enrich to add book details from database
  - Choice (Route) for conditional processing
  - Dead Letter Channel for error handling

## Event Types

| Event Type | Topic | Description |
|------------|-------|-------------|
| `BOOK_ADDED` | `bookstore-books` | New book added to catalog |
| `ORDER_CREATED` | `bookstore-orders` | Customer order placed |
| `SHIPMENT_CREATED` | `bookstore-shipments` | Order prepared for shipping |

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

## Database Setup

The SQLite database is automatically initialized on application startup with:

- **Schema**: `src/main/resources/db/schema.sql` (books, customers, orders tables)
- **Sample Data**: `src/main/resources/db/data.sql` (15 books, 5 customers, 3 orders)

The database file is created at `bookstore.db` in the project directory.

## Running the Project

Once the infrastructure is up, run the Camel application:

```bash
mvn camel:run
```

## Configuration

Key configuration options in `src/main/resources/application.properties`:

```properties
# Kafka
kafka.brokers = localhost:9092
kafka.topic.books = bookstore-books
kafka.topic.orders = bookstore-orders

# Apicurio Registry
apicurio.registry.url = http://localhost:8080/apis/registry/v2

# SQLite
sqlite.jdbc.url = jdbc:sqlite:bookstore.db
```

## Camel Routes

| Route ID | Description |
|----------|-------------|
| `init-database` | Initializes SQLite database on startup |
| `produce-book-added` | Publishes book catalog updates to Kafka |
| `produce-order-created` | Publishes customer orders to Kafka |
| `process-orders` | Consumes and processes orders with EIP patterns |
| `get-book-details` | Queries book details from SQLite database |
| `process-shipments` | Handles shipment notifications |
| `consume-and-report` | Aggregates statistics over time |

## Project Structure

```
src/
├── main/
│   ├── java/com/example/bookstore/
│   │   ├── models/          # POJOs (Book, Order, Customer, etc.)
│   │   ├── service/         # Business logic (BookService)
│   │   └── strategy/        # Camel strategies (filters, aggregators)
│   ├── resources/
│   │   ├── camel/           # Camel routes (routes.xml)
│   │   ├── db/              # Database schema and data
│   │   └── schemas/         # JSON Schema definitions
│   └── main/resources/
└── main/java/
```

## Documentation

- [Bookstore Guide](guide_bookstore.md) - Detailed documentation of the bookstore implementation
- [Apicurio Registry Guide](guide_apicuro.md) - Apicurio Registry integration guide
- [Kafka Guide](guide_kafka.md) - Kafka integration guide

## Key Improvements Over Original Example

| Aspect | Original (UserEvent) | New (Bookstore) |
|--------|---------------------|-----------------|
| Schema Complexity | Simple flat object | Nested objects, union types |
| Event Types | 1 type | 3 distinct event types |
| Topics | 1 topic | 5 topics with routing |
| EIP Patterns | None | Split, Filter, Enrich, Choice, Aggregate |
| Database | None | SQLite with sample data |
| Error Handling | Basic try-catch | Dead Letter Channel |

## Next Steps

1. **Add Database Integration**: Connect to a real book catalog database
2. **Implement Payment Processing**: Integrate with payment gateway APIs
3. **Add Monitoring**: Use Camel's metrics component for observability
4. **Implement CQRS**: Separate read and write models for scalability
