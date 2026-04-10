package com.example.bookstore.models;

import java.util.List;
import java.util.Objects;

/**
 * Represents an order placed by a customer.
 */
public class Order {
    
    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private Double subtotal;
    private Double tax;
    private Double shippingCost;
    private Double totalAmount;
    private Customer.Address shippingAddress;

    // Getters and Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getTax() { return tax; }
    public void setTax(Double tax) { this.tax = tax; }

    public Double getShippingCost() { return shippingCost; }
    public void setShippingCost(Double shippingCost) { this.shippingCost = shippingCost; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public Customer.Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Customer.Address shippingAddress) { this.shippingAddress = shippingAddress; }

    /**
     * Validates the order has at least one item and totals are correct.
     */
    public boolean isValid() {
        if (items == null || items.isEmpty()) return false;
        
        double calculatedSubtotal = items.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
        
        return Math.abs(calculatedSubtotal - subtotal) < 0.01;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", items=" + items +
                ", subtotal=" + subtotal +
                ", tax=" + tax +
                ", shippingCost=" + shippingCost +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
