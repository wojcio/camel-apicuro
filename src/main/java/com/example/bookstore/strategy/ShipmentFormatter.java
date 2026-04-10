package com.example.bookstore.strategy;

import com.example.bookstore.models.BookstoreEvent;
import com.example.bookstore.models.Shipment;

/**
 * Formatter for shipment labels and messages.
 */
public class ShipmentFormatter {

    /**
     * Formats a shipment event into a readable shipping label.
     */
    public String format(BookstoreEvent<Shipment> event) {
        Shipment shipment = event.getPayload();
        if (shipment == null) {
            return "Invalid shipment";
        }
        
        return String.format(
            "[%s] Order: %s | Carrier: %s | Service: %s | Tracking: %s | Est. Delivery: %s",
            shipment.getShipmentId(),
            shipment.getOrderId(),
            shipment.getCarrier(),
            shipment.getServiceLevel(),
            shipment.getTrackingNumber(),
            shipment.getEstimatedDelivery()
        );
    }
}
