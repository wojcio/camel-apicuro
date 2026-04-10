# Bookstore Event Streaming with Apache Camel

This project demonstrates a **Bookstore Event Streaming** system using Apache Camel XML DSL, integrated with:

- **Apicurio Registry** for JSON Schema management
- **SQLite** for persistent data storage
- **Netty HTTP** for REST API endpoints
- **Apache Kafka** for event streaming

## Features

- **Camel XML DSL**: Routes are defined in `src/main/resources/camel/`.
- **JSON Schema Validation**: Uses Apicurio's JSON Schema Kafka SerDes.
- **SQLite Database**: Persistent book catalog with sample data (`src/main/resources/db/`).
- **REST API**: HTTP endpoints for managing books and orders (port 8081).
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
# HTTP Server (Netty API)
http.port = 8081

# Kafka
kafka.brokers = localhost:9092
kafka.topic.books = bookstore-books
kafka.topic.orders = bookstore-orders

# Apicurio Registry
apicurio.registry.url = http://localhost:8080/apis/registry/v2

# SQLite
sqlite.jdbc.url = jdbc:sqlite:bookstore.db
```

## REST API Endpoints

### Books

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/books` | Get all books |
| GET | `/api/books/{isbn}` | Get book by ISBN |
| POST | `/api/books` | Add new book |
| PUT | `/api/books/{isbn}/stock` | Update stock |

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{orderId}` | Get order by ID |
| GET | `/api/orders/{orderId}/items` | Get order items |
| POST | `/api/orders` | Create new order |

### API Examples

```bash
# Get all books
curl http://localhost:8081/api/books

# Add a new book
curl -X POST http://localhost:8081/api/books \
  -H "Content-Type: application/json" \
  -d '{"isbn":"9781234567890","title":"New Book","author":"Author","genre":"Fiction","price":19.99,"stock":50}'

# Get all orders
curl http://localhost:8081/api/orders

# Create a new order
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": "ORD-NEW-001",
    "customerId": "CUST-001",
    "items": [{"isbn":"9781234567890","quantity":2,"unitPrice":49.99}],
    "subtotal": 99.98,
    "tax": 8.00,
    "shippingCost": 5.99,
    "totalAmount": 113.97
  }'
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
│   │   ├── service/         # Business logic (BookService, OrderValidator)
│   │   └── strategy/        # Camel strategies (filters, aggregators)
│   ├── resources/
│   │   ├── camel/           # Camel routes (routes.xml, api.xml)
│   │   ├── db/              # Database schema and data
│   │   └── schemas/         # JSON Schema definitions
└── main/resources/
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
| REST API | None | Full CRUD for books and orders |
| Error Handling | Basic try-catch | Dead Letter Channel |

## Next Steps

1. **Add Database Integration**: Connect to a real book catalog database
2. **Implement Payment Processing**: Integrate with payment gateway APIs
3. **Add Monitoring**: Use Camel's metrics component for observability
4. **Implement CQRS**: Separate read and write models for scalability
