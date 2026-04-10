package com.example.bookstore.strategy;

import com.example.bookstore.models.BookstoreEvent;
import com.example.bookstore.models.Shipment;

/**
 * Filter to identify and process shipment events.
 */
public class ShipmentFilter {

    /**
     * Checks if the event is a SHIPMENT_CREATED event.
     */
    public boolean isShipmentCreated(BookstoreEvent<?> event) {
        return "SHIPMENT_CREATED".equals(event.getEventType());
    }

    /**
     * Checks if the shipment uses expedited shipping.
     */
    public boolean isExpedited(BookstoreEvent<Shipment> event) {
        Shipment shipment = event.getPayload();
        return shipment != null && 
               ("Expedited".equals(shipment.getServiceLevel()) || 
                "Priority".equals(shipment.getServiceLevel()));
    }
}
