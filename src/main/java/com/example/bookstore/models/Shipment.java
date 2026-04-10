package com.example.bookstore.models;

import java.util.List;
import java.util.Objects;

/**
 * Represents a shipment for an order.
 */
public class Shipment {
    
    private String shipmentId;
    private String orderId;
    private String carrier;
    private String serviceLevel;
    private String trackingNumber;
    private String estimatedDelivery;
    private List<ShipmentItem> items;

    // Getters and Setters
    public String getShipmentId() { return shipmentId; }
    public void setShipmentId(String shipmentId) { this.shipmentId = shipmentId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }

    public String getServiceLevel() { return serviceLevel; }
    public void setServiceLevel(String serviceLevel) { this.serviceLevel = serviceLevel; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(String estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }

    public List<ShipmentItem> getItems() { return items; }
    public void setItems(List<ShipmentItem> items) { this.items = items; }

    /**
     * Calculates total items in shipment.
     */
    public int getTotalQuantity() {
        return items.stream().mapToInt(ShipmentItem::getQuantity).sum();
    }

    /**
     * Checks if shipment uses expedited shipping.
     */
    public boolean isExpedited() {
        return "Expedited".equals(serviceLevel) || "Priority".equals(serviceLevel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return Objects.equals(shipmentId, shipment.shipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shipmentId);
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "shipmentId='" + shipmentId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", carrier='" + carrier + '\'' +
                ", serviceLevel='" + serviceLevel + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", estimatedDelivery='" + estimatedDelivery + '\'' +
                ", items=" + items +
                '}';
    }

    /**
     * Inner class for shipment item.
     */
    public static class ShipmentItem {
        private String isbn;
        private Integer quantity;

        // Getters and Setters
        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        @Override
        public String toString() {
            return "ShipmentItem{" +
                    "isbn='" + isbn + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}
