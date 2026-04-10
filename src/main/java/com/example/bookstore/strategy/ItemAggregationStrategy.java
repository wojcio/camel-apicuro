package com.example.bookstore.strategy;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import com.example.bookstore.models.Book;

/**
 * Aggregates book details with order items.
 */
public class ItemAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange original, Exchange resource) {
        if (resource == null || resource.getException() != null) {
            return original;
        }
        
        // Get the order item from original exchange
        Book bookDetails = resource.getIn().getBody(Book.class);
        
        if (bookDetails != null) {
            // Add book details to the order item
            original.getIn().setBody(bookDetails);
        }
        
        return original;
    }
}
