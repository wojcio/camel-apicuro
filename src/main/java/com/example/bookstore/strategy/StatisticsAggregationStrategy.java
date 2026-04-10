package com.example.bookstore.strategy;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import com.example.bookstore.models.BookstoreEvent;

/**
 * Aggregates statistics across all bookstore events over time.
 */
public class StatisticsAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange original, Exchange resource) {
        Statistics stats;
        
        if (original.getIn().getBody() instanceof Statistics) {
            stats = original.getIn().getBody(Statistics.class);
        } else {
            stats = new Statistics();
        }
        
        // Process the incoming event
        if (resource != null && resource.getIn().getBody() instanceof BookstoreEvent) {
            BookstoreEvent<?> event = resource.getIn().getBody(BookstoreEvent.class);
            
            if ("BOOK_ADDED".equals(event.getEventType())) {
                stats.booksAdded++;
            } else if ("ORDER_CREATED".equals(event.getEventType())) {
                stats.ordersProcessed++;
                
                // Extract total amount from order
                Object payload = event.getPayload();
                if (payload instanceof com.example.bookstore.models.Order) {
                    com.example.bookstore.models.Order order = 
                        (com.example.bookstore.models.Order) payload;
                    stats.totalRevenue += order.getTotalAmount();
                }
            } else if ("SHIPMENT_CREATED".equals(event.getEventType())) {
                stats.shipmentsCreated++;
            }
        }
        
        original.getIn().setBody(stats);
        return original;
    }

    /**
     * Statistics class to hold aggregated data.
     */
    public static class Statistics {
        public int booksAdded = 0;
        public int ordersProcessed = 0;
        public int shipmentsCreated = 0;
        public double totalRevenue = 0.0;

        @Override
        public String toString() {
            return String.format(
                "Statistics{booksAdded=%d, ordersProcessed=%d, shipmentsCreated=%d, totalRevenue=%.2f}",
                booksAdded, ordersProcessed, shipmentsCreated, totalRevenue
            );
        }
    }
}
