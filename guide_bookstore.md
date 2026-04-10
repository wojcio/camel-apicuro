# Bookstore Event Streaming with Apache Camel

This guide explains the bookstore event streaming implementation using Apache Camel XML DSL and Apicurio Registry for schema management.

## Overview

The bookstore application demonstrates real-world event streaming patterns including:
- Multiple Kafka topics for different event types
- JSON Schema validation with Apicurio Registry
- Apache Camel EIP (Enterprise Integration Patterns)
- POJO marshalling/unmarshalling with Jackson
- Error handling with Dead Letter Channel

## Event Types

### 1. Book Added Event (`BOOK_ADDED`)
Published when a new book is added to the catalog.

```json
{
  "eventId": "...",
  "eventType": "BOOK_ADDED",
  "timestamp": 1234567890,
  "book": {
    "isbn": "9781234567890",
    "title": "The Art of Java Programming",
    "author": "Jane Developer",
    "genre": "Technology",
    "price": 49.99,
    "stock": 100
  }
}
```

### 2. Order Created Event (`ORDER_CREATED`)
Published when a customer places an order with multiple items.

```json
{
  "eventId": "...",
  "eventType": "ORDER_CREATED",
  "timestamp": 1234567890,
  "order": {
    "orderId": "ORD-...",
    "customerId": "CUST-12345",
    "items": [
      {
        "isbn": "9781234567890",
        "quantity": 2,
        "unitPrice": 49.99
      }
    ],
    "subtotal": 129.97,
    "tax": 10.40,
    "shippingCost": 5.99,
    "totalAmount": 146.36
  }
}
```

### 3. Shipment Created Event (`SHIPMENT_CREATED`)
Published when an order is prepared for shipping.

```json
{
  "eventId": "...",
  "eventType": "SHIPMENT_CREATED",
  "timestamp": 1234567890,
  "shipment": {
    "shipmentId": "SHIP-...",
    "orderId": "ORD-...",
    "carrier": "FedEx",
    "serviceLevel": "Expedited",
    "trackingNumber": "1Z999AA10123456784",
    "estimatedDelivery": "2024-01-25"
  }
}
```

## Kafka Topics

| Topic | Purpose |
|-------|---------|
| `bookstore-books` | New books added to catalog |
| `bookstore-orders` | Customer orders |
| `bookstore-shipments` | Shipment notifications |
| `bookstore-dlq` | Dead Letter Queue for failed messages |
| `bookstore-expedited` | Expedited shipments (filtered) |

## Camel EIP Patterns Demonstrated

### 1. Split-Aggregate Pattern
Orders are split into individual items for processing, then re-aggregated:

```xml
<split>
    <simple>${body.payload.items}</simple>
    <!-- Process each item -->
</split>
<aggregate completionSize="2">
    <!-- Reassemble order -->
</aggregate>
```

### 2. Filter Pattern
Only process specific event types:

```xml
<filter>
    <method ref="orderFilter" method="isOrderCreated"/>
    <!-- Process only ORDER_CREATED events -->
</filter>
```

### 3. Enrich Pattern
Add book details from catalog to order items:

```xml
<enrich uri="direct:get-book-details" strategyRef="itemAggregationStrategy"/>
```

### 4. Choice (Route) Pattern
Different processing based on conditions:

```xml
<choice>
    <when>
        <simple>${body.price} &gt; 50</simple>
        <!-- High-value item handling -->
    </when>
    <otherwise>
        <!-- Standard item handling -->
    </otherwise>
</choice>
```

### 5. Dead Letter Channel
Error handling for failed messages:

```xml
<onException>
    <exception>java.lang.Exception</exception>
    <handled><constant>true</constant></handled>
    <to uri="kafka:bookstore-dlq"/>
</onException>
```

## POJO Models

The application uses Jackson for JSON serialization/deserialization:

| Class | Description |
|-------|-------------|
| `BookstoreEvent<T>` | Wrapper for all events with metadata |
| `Book` | Book catalog information |
| `Order` | Customer order with items |
| `OrderItem` | Individual item in an order |
| `Customer` | Customer with shipping address |
| `Shipment` | Shipping information |

## Strategy Classes

| Class | Purpose |
|-------|---------|
| `OrderFilter` | Identifies ORDER_CREATED events |
| `InventoryFilter` | Checks book availability |
| `ShipmentFilter` | Identifies and filters shipments |
| `ShipmentFormatter` | Formats shipping labels |
| `ItemAggregationStrategy` | Combines items with book details |
| `OrderAggregationStrategy` | Reassembles processed orders |
| `StatisticsAggregationStrategy` | Collects metrics over time |

## Running the Application

### 1. Start Infrastructure
```bash
podman-compose up -d
```

This starts:
- **Kafka** on `localhost:9092`
- **Apicurio Registry** on `http://localhost:8080`

### 2. Run the Camel Application
```bash
mvn camel:run
```

### 3. Monitor Events

Check Apicurio Registry UI:
```bash
open http://localhost:8080/ui
```

List Kafka topics:
```bash
podman exec -it kafka /opt/bitnami/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 --list
```

Consume messages manually:
```bash
podman exec -it kafka /opt/bitnami/kafka/bin/kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 --topic bookstore-orders \
  --from-beginning
```

## Key Improvements Over Original Example

| Aspect | Original (UserEvent) | New (Bookstore) |
|--------|---------------------|-----------------|
| Schema Complexity | Simple flat object | Nested objects, union types |
| Event Types | 1 type | 3 distinct event types |
| Topics | 1 topic | 5 topics with routing |
| EIP Patterns | None | Split, Filter, Enrich, Choice, Aggregate |
| Error Handling | Basic try-catch | Dead Letter Channel |
| Data Transformation | None | POJO marshalling/unmarshalling |
| Business Logic | None | Inventory checks, price filtering |

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐
│   Producer  │────▶│ bookstore-books│    │   Consumer   │
│ (Book Added)│     └──────────────┘     │  (Report)    │
└─────────────┘                          └──────────────┘

┌─────────────┐     ┌──────────────┐     ┌──────────────┐
│   Producer  │────▶│ bookstore-orders│   │  Processor   │
│ (Order)     │     └──────────────┘   ┌▶│  (Split/    │
└─────────────┘                          │ │ Filter/     │
                                         │ │ Enrich)     │
┌─────────────┐     ┌──────────────┐   │ └──────────────┘
│   Producer  │────▶│ bookstore-shipments│ │
│ (Shipment)  │     └──────────────┘   │ ┌──────────────┐
└─────────────┘                          │▶│  Filter by   │
                                         │ │ Expedited    │
                                         │ └──────────────┘
┌─────────────┐     ┌──────────────┐   │
│   Producer  │────▶│ bookstore-dlq  │◀─┘
│ (Error)     │     └──────────────┘
└─────────────┘

┌─────────────┐     ┌──────────────┐
│   Producer  │────▶│ bookstore-    │
│ (Expedited) │     │ expedited    │
└─────────────┘     └──────────────┘
```

## Next Steps

1. **Add Database Integration**: Connect to a real book catalog database
2. **Implement Payment Processing**: Integrate with payment gateway APIs
3. **Add Monitoring**: Use Camel's metrics component for observability
4. **Implement CQRS**: Separate read and write models for scalability
5. **Add Event Sourcing**: Store all events for audit trail
