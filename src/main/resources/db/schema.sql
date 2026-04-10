-- SQLite Database Schema for Bookstore Application
-- This file contains the table definitions for the bookstore database

-- Drop tables if they exist (for clean initialization)
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS books;

-- Books table
CREATE TABLE books (
    isbn TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    genre TEXT CHECK(genre IN ('Fiction', 'Non-Fiction', 'Science', 'History', 'Fantasy', 'Romance', 'Technology')) NOT NULL,
    price REAL NOT NULL CHECK(price >= 0),
    stock INTEGER NOT NULL DEFAULT 0 CHECK(stock >= 0),
    published_date TEXT
);

-- Customers table
CREATE TABLE customers (
    customer_id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    street TEXT NOT NULL,
    city TEXT NOT NULL,
    state TEXT NOT NULL CHECK(LENGTH(state) = 2),
    zip_code TEXT NOT NULL,
    country TEXT NOT NULL DEFAULT 'US'
);

-- Orders table
CREATE TABLE orders (
    order_id TEXT PRIMARY KEY,
    customer_id TEXT NOT NULL,
    order_date TEXT NOT NULL DEFAULT (datetime('now')),
    subtotal REAL NOT NULL CHECK(subtotal >= 0),
    tax REAL NOT NULL DEFAULT 0,
    shipping_cost REAL NOT NULL DEFAULT 0,
    total_amount REAL NOT NULL CHECK(total_amount >= 0),
    shipping_address TEXT NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Order items table
CREATE TABLE order_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id TEXT NOT NULL,
    isbn TEXT NOT NULL,
    quantity INTEGER NOT NULL CHECK(quantity > 0),
    unit_price REAL NOT NULL CHECK(unit_price >= 0),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (isbn) REFERENCES books(isbn)
);

-- Create indexes for better query performance
CREATE INDEX idx_books_genre ON books(genre);
CREATE INDEX idx_books_author ON books(author);
CREATE INDEX idx_orders_customer ON orders(customer_id);
