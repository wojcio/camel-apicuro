package com.example.bookstore.strategy;

import com.example.bookstore.models.Book;

/**
 * Filter to check if a book is in stock.
 */
public class InventoryFilter {

    /**
     * Checks if the book has available stock.
     */
    public boolean isInStock(Book book) {
        return book != null && book.getStock() != null && book.getStock() > 0;
    }
}
