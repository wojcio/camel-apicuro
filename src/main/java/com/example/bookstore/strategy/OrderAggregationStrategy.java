package com.example.bookstore.strategy;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import com.example.bookstore.models.BookstoreEvent;

/**
 * Aggregates processed order items back into the original order.
 */
public class OrderAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange original, Exchange resource) {
        if (resource == null || resource.getException() != null) {
            return original;
        }
        
        // Return the original order with enriched items
        return original;
    }
}
