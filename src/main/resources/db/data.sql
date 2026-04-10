-- SQLite Database Sample Data for Bookstore Application
-- This file contains sample data to populate the bookstore database

-- Insert sample books (15 books across different genres)
INSERT INTO books (isbn, title, author, genre, price, stock, published_date) VALUES
-- Technology books
('9781234567890', 'The Art of Java Programming', 'Jane Developer', 'Technology', 49.99, 100, '2024-01-15'),
('9781234567891', 'Python for Data Science', 'John Analyst', 'Technology', 29.99, 75, '2024-02-20'),
('9781234567892', 'Modern JavaScript Essentials', 'Alex Coder', 'Technology', 39.99, 50, '2024-03-10'),
('9781234567893', 'Cloud Architecture Patterns', 'Sarah Cloud', 'Technology', 54.99, 30, '2024-01-05'),
('9781234567894', 'Database Design Best Practices', 'Mike Schema', 'Technology', 44.99, 45, '2023-12-15'),

-- Fiction books
('9781234567900', 'The Lost City of Z', 'Tom Adventure', 'Fiction', 14.99, 200, '2023-06-01'),
('9781234567901', 'Murder on the Orient Express', 'Agatha Mystery', 'Fiction', 12.99, 150, '2023-07-15'),
('9781234567902', 'The Great Gatsby 2: The Lost Chapter', 'F. Scott Writer', 'Fiction', 15.99, 80, '2024-04-01'),

-- Science books
('9781234567910', 'A Brief History of Time', 'Stephen Hawking', 'Science', 18.99, 60, '2023-05-20'),
('9781234567911', 'The Origin of Species', 'Charles Darwin', 'Science', 16.99, 40, '2023-08-10'),
('9781234567912', 'Cosmos', 'Carl Sagan', 'Science', 21.99, 55, '2023-09-05'),

-- History books
('9781234567920', 'The Guns of August', 'Barbara Tuchman', 'History', 22.99, 35, '2023-10-01'),
('9781234567921', 'SPQR: A History of Ancient Rome', 'Mary Beard', 'History', 24.99, 45, '2023-11-15'),

-- Fantasy books
('9781234567930', 'The Name of the Wind', 'Patrick Rothfuss', 'Fantasy', 19.99, 90, '2023-04-10'),
('9781234567931', 'The Way of Kings', 'Brandon Sanderson', 'Fantasy', 26.99, 70, '2023-05-01'),

-- Romance books
('9781234567940', 'Pride and Prejudice', 'Jane Austen', 'Romance', 11.99, 120, '2023-03-15'),
('9781234567941', 'The Hating Game', 'Sally Thorne', 'Romance', 13.99, 85, '2024-01-20');

-- Insert sample customers (5 customers)
INSERT INTO customers (customer_id, name, email, street, city, state, zip_code, country) VALUES
('CUST-001', 'John Smith', 'john.smith@email.com', '123 Main Street', 'New York', 'NY', '10001', 'US'),
('CUST-002', 'Emily Johnson', 'emily.j@email.com', '456 Oak Avenue', 'Los Angeles', 'CA', '90001', 'US'),
('CUST-003', 'Michael Brown', 'm.brown@email.com', '789 Pine Road', 'Chicago', 'IL', '60601', 'US'),
('CUST-004', 'Sarah Davis', 'sarah.d@email.com', '321 Elm Boulevard', 'Houston', 'TX', '77001', 'US'),
('CUST-005', 'David Wilson', 'd.wilson@email.com', '654 Maple Drive', 'Phoenix', 'AZ', '85001', 'US');

-- Insert sample orders (3 orders)
INSERT INTO orders (order_id, customer_id, order_date, subtotal, tax, shipping_cost, total_amount, shipping_address) VALUES
('ORD-001', 'CUST-001', '2024-04-01 10:30:00', 79.98, 6.40, 5.99, 92.37, '{"street":"123 Main Street","city":"New York","state":"NY","zipCode":"10001","country":"US"}'),
('ORD-002', 'CUST-002', '2024-04-02 14:20:00', 44.99, 3.60, 5.99, 54.58, '{"street":"456 Oak Avenue","city":"Los Angeles","state":"CA","zipCode":"90001","country":"US"}'),
('ORD-003', 'CUST-003', '2024-04-03 09:15:00', 71.98, 5.76, 5.99, 83.73, '{"street":"789 Pine Road","city":"Chicago","state":"IL","zipCode":"60601","country":"US"}');

-- Insert order items
INSERT INTO order_items (order_id, isbn, quantity, unit_price) VALUES
-- Order 001 items
('ORD-001', '9781234567890', 1, 49.99),
('ORD-001', '9781234567891', 1, 29.99),
-- Order 002 items
('ORD-002', '9781234567893', 1, 44.99),
-- Order 003 items
('ORD-003', '9781234567900', 2, 14.99),
('ORD-003', '9781234567910', 1, 18.99);
