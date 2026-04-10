package com.example.bookstore.strategy;

import com.example.bookstore.models.BookstoreEvent;

/**
 * Filter to identify ORDER_CREATED events.
 */
public class OrderFilter {

    /**
     * Checks if the event is an ORDER_CREATED event.
     */
    public boolean isOrderCreated(BookstoreEvent<?> event) {
        return "ORDER_CREATED".equals(event.getEventType());
    }
}
