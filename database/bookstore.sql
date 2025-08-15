-- Create Database
CREATE DATABASE IF NOT EXISTS bookstore;
USE bookstore;

-- Table: customers
CREATE TABLE customers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: addresses
CREATE TABLE addresses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    street VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(50),
    zip_code VARCHAR(20),
    country VARCHAR(100),
    is_default BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Table: accounts
CREATE TABLE accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL UNIQUE,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('CUSTOMER', 'ADMIN') DEFAULT 'CUSTOMER',
    is_active BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Table: books
CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT DEFAULT 0,
    category VARCHAR(50),
    description TEXT,
    image_url VARCHAR(500),
    published_date DATE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: orders
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    shipping_address VARCHAR(500),
    payment_method VARCHAR(50),
    notes TEXT,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Table: order_items
CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- Create indexes for better performance
CREATE INDEX idx_customer_email ON customers(email);
CREATE INDEX idx_account_username ON accounts(username);
CREATE INDEX idx_book_title ON books(title);
CREATE INDEX idx_book_author ON books(author);
CREATE INDEX idx_order_customer ON orders(customer_id);
CREATE INDEX idx_order_status ON orders(status);

-- Insert sample data for testing

-- Insert sample customers
INSERT INTO customers (name, email, phone) VALUES
('Admin User', 'admin@bookstore.com', '0123456789'),
('John Doe', 'john.doe@email.com', '0987654321'),
('Jane Smith', 'jane.smith@email.com', '0123456780'),
('Bob Johnson', 'bob.j@email.com', '0987654322');

-- Insert sample addresses
INSERT INTO addresses (customer_id, street, city, state, zip_code, country, is_default) VALUES
(1, '123 Admin St', 'Admin City', 'AC', '10001', 'USA', TRUE),
(2, '456 Main St', 'New York', 'NY', '10002', 'USA', TRUE),
(3, '789 Oak Ave', 'Los Angeles', 'CA', '90001', 'USA', TRUE),
(4, '321 Pine St', 'Chicago', 'IL', '60601', 'USA', TRUE);

-- Insert sample accounts (password is 'password123' hashed with simple MD5 for demo)
-- In production, use proper password hashing like BCrypt
INSERT INTO accounts (customer_id, username, password, role) VALUES
(1, 'admin', 'admin123', 'ADMIN'),
(2, 'johndoe', 'password123', 'CUSTOMER'),
(3, 'janesmith', 'password123', 'CUSTOMER'),
(4, 'bobj', 'password123', 'CUSTOMER');

-- Insert sample books
INSERT INTO books (title, author, isbn, price, quantity, category, description, image_url) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 12.99, 50, 'Fiction', 'A classic American novel set in the Jazz Age.', '/images/great-gatsby.jpg'),
('To Kill a Mockingbird', 'Harper Lee', '9780061120084', 14.99, 45, 'Fiction', 'A gripping tale of racial injustice and childhood innocence.', '/images/mockingbird.jpg'),
('1984', 'George Orwell', '9780451524935', 13.99, 60, 'Fiction', 'A dystopian social science fiction novel.', '/images/1984.jpg'),
('Pride and Prejudice', 'Jane Austen', '9780141040349', 11.99, 35, 'Romance', 'A romantic novel of manners.', '/images/pride-prejudice.jpg'),
('The Catcher in the Rye', 'J.D. Salinger', '9780316769174', 12.99, 40, 'Fiction', 'A story about teenage rebellion and angst.', '/images/catcher-rye.jpg'),
('Harry Potter and the Sorcerer\'s Stone', 'J.K. Rowling', '9780439708180', 15.99, 100, 'Fantasy', 'The first book in the Harry Potter series.', '/images/harry-potter-1.jpg'),
('The Hobbit', 'J.R.R. Tolkien', '9780547928227', 14.99, 55, 'Fantasy', 'A fantasy novel and children\'s book.', '/images/hobbit.jpg'),
('The Da Vinci Code', 'Dan Brown', '9780307474278', 13.99, 70, 'Thriller', 'A mystery thriller novel.', '/images/davinci-code.jpg'),
('The Alchemist', 'Paulo Coelho', '9780062315007', 12.99, 65, 'Fiction', 'A novel about following your dreams.', '/images/alchemist.jpg'),
('Gone Girl', 'Gillian Flynn', '9780307588371', 14.99, 48, 'Thriller', 'A psychological thriller about a marriage gone wrong.', '/images/gone-girl.jpg');

-- Insert sample orders
INSERT INTO orders (customer_id, total_amount, status, shipping_address, payment_method) VALUES
(2, 41.97, 'DELIVERED', '456 Main St, New York, NY 10002', 'Credit Card'),
(3, 28.98, 'SHIPPED', '789 Oak Ave, Los Angeles, CA 90001', 'PayPal'),
(2, 15.99, 'PENDING', '456 Main St, New York, NY 10002', 'Credit Card');

-- Insert sample order items
INSERT INTO order_items (order_id, book_id, quantity, price, subtotal) VALUES
(1, 1, 1, 12.99, 12.99),
(1, 3, 1, 13.99, 13.99),
(1, 5, 1, 12.99, 12.99),
(2, 2, 1, 14.99, 14.99),
(2, 4, 1, 11.99, 11.99),
(3, 6, 1, 15.99, 15.99);

-- Create view for order details
CREATE VIEW order_details_view AS
SELECT 
    o.id AS order_id,
    o.order_date,
    o.status,
    o.total_amount,
    c.name AS customer_name,
    c.email AS customer_email,
    b.title AS book_title,
    b.author AS book_author,
    oi.quantity,
    oi.price,
    oi.subtotal
FROM orders o
JOIN customers c ON o.customer_id = c.id
JOIN order_items oi ON o.id = oi.order_id
JOIN books b ON oi.book_id = b.id;

-- Stored procedure to get bestselling books
DELIMITER //
CREATE PROCEDURE GetBestSellingBooks(IN limit_count INT)
BEGIN
    SELECT 
        b.id,
        b.title,
        b.author,
        b.price,
        COUNT(oi.book_id) AS times_ordered,
        SUM(oi.quantity) AS total_quantity_sold
    FROM books b
    JOIN order_items oi ON b.id = oi.book_id
    GROUP BY b.id
    ORDER BY total_quantity_sold DESC
    LIMIT limit_count;
END //
DELIMITER ;