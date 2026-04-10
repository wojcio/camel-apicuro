package com.example.bookstore.models;

import java.util.Objects;

/**
 * Represents a bookstore event with metadata.
 */
public class BookstoreEvent<T> {
    
    private String eventId;
    private String eventType;
    private Long timestamp;
    private T payload;

    // Getters and Setters
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public T getPayload() { return payload; }
    public void setPayload(T payload) { this.payload = payload; }

    /**
     * Checks if the event is of a specific type.
     */
    public boolean isType(String type) {
        return eventType != null && eventType.equals(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookstoreEvent<?> that = (BookstoreEvent<?>) o;
        return Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }

    @Override
    public String toString() {
        return "BookstoreEvent{" +
                "eventId='" + eventId + '\'' +
                ", eventType='" + eventType + '\'' +
                ", timestamp=" + timestamp +
                ", payload=" + payload +
                '}';
    }
}
