package com.example.bookstore.service;

import com.example.bookstore.models.Order;

/**
 * Validator for order objects.
 */
public class OrderValidator {

    /**
     * Checks if the order is valid (has at least one item).
     */
    public boolean isValid(Order order) {
        return order != null && 
               order.getItems() != null && 
               !order.getItems().isEmpty();
    }
}
