package com.example.bookstore.service;

import com.example.bookstore.models.Book;
import org.apache.camel.ProducerTemplate;

import java.util.List;
import java.util.Map;

/**
 * Service class for book-related database operations.
 */
public class BookService {

    private final ProducerTemplate producerTemplate;

    public BookService(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    /**
     * Get a book by its ISBN from the database.
     *
     * @param isbn The ISBN of the book to retrieve
     * @return The Book object or null if not found
     */
    public Book getBookByIsbn(String isbn) {
        String sql = "SELECT isbn, title, author, genre, price, stock, published_date as publishedDate FROM books WHERE isbn = :isbn";
        return producerTemplate.requestBody("sql:" + sql + "?outputType=SELECT_ONE", isbn, Book.class);
    }

    /**
     * Get all books from the database.
     *
     * @return List of all books
     */
    public List<Book> getAllBooks() {
        String sql = "SELECT isbn, title, author, genre, price, stock, published_date as publishedDate FROM books ORDER BY title";
        return producerTemplate.requestBody("sql:" + sql, null, List.class);
    }

    /**
     * Get books by genre.
     *
     * @param genre The genre to filter by
     * @return List of books in the specified genre
     */
    public List<Book> getBooksByGenre(String genre) {
        String sql = "SELECT isbn, title, author, genre, price, stock, published_date as publishedDate FROM books WHERE genre = :genre ORDER BY title";
        return producerTemplate.requestBody("sql:" + sql, genre, List.class);
    }

    /**
     * Update book stock after an order.
     *
     * @param isbn The ISBN of the book
     * @param quantity The quantity to decrease
     * @return true if update was successful, false otherwise
     */
    public boolean updateStock(String isbn, int quantity) {
        String sql = "UPDATE books SET stock = stock - :quantity WHERE isbn = :isbn AND stock >= :quantity";
        int rowsAffected = producerTemplate.requestBody("sql:" + sql, 
            Map.of("isbn", isbn, "quantity", quantity), Integer.class);
        return rowsAffected > 0;
    }

    /**
     * Get books with low stock (less than 10).
     *
     * @return List of books with low stock
     */
    public List<Book> getLowStockBooks() {
        String sql = "SELECT isbn, title, author, genre, price, stock, published_date as publishedDate FROM books WHERE stock < 10 ORDER BY stock";
        return producerTemplate.requestBody("sql:" + sql, null, List.class);
    }

    /**
     * Check if a book is in stock.
     *
     * @param isbn The ISBN to check
     * @return true if in stock, false otherwise
     */
    public boolean isInStock(String isbn) {
        String sql = "SELECT COUNT(*) FROM books WHERE isbn = :isbn AND stock > 0";
        Integer count = producerTemplate.requestBody("sql:" + sql + "?outputType=Scalar", isbn, Integer.class);
        return count != null && count > 0;
    }
}
