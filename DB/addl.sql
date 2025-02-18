CREATE TABLE invoice (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name CHAR(3) NOT NULL,
    customer_contact VARCHAR(20) NOT NULL,
    invoice_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) DEFAULT 0,
    tax DECIMAL(10,2) DEFAULT 0,
    grand_total DECIMAL(10,2) NOT NULL,
    payment_status ENUM('Pending', 'Paid', 'Cancelled') DEFAULT 'Pending',
    payment_method CHAR(4) NOT NULL,
    notes CHAR(10) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE invoice_item (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    invoice_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES stock(product_id) ON DELETE CASCADE
);

INSERT INTO invoice (customer_name, customer_contact, total_amount, discount, tax, grand_total, payment_status, payment_method, notes)
VALUES
('XYP', '9876543210', 5000.00, 200.00, 300.00, 5100.00, 'Paid', 'AB12', 'A1B2C3D4E5'),
('QWE', '8765432109', 12000.00, 500.00, 900.00, 12400.00, 'Pending', 'ZX78', 'X8Y7Z6W5V4'),
('RTM', '7654321098', 8000.00, 300.00, 600.00, 8300.00, 'Paid', 'RT45', 'P4L3K2D1N0'),
('LZY', '6543210987', 15000.00, 700.00, 1200.00, 15700.00, 'Cancelled', 'BN23', 'M9N8O7R6S5'),
('PLK', '5432109876', 22000.00, 800.00, 1500.00, 22700.00, 'Paid', 'CX89', 'J1A2B3L4K5');


INSERT INTO invoice_item (invoice_id, product_id, quantity, unit_price, total_price)
VALUES
(1, 1, 2, 1000.00, 2000.00),
(1, 3, 1, 1500.00, 1500.00),
(2, 2, 3, 1200.00, 3600.00),
(2, 4, 2, 1000.00, 2000.00),
(3, 5, 4, 800.00, 3200.00),
(3, 6, 2, 1000.00, 2000.00),
(4, 7, 3, 500.00, 1500.00),
(4, 8, 2, 600.00, 1200.00),
(5, 9, 5, 1500.00, 7500.00),
(5, 10, 1, 1700.00, 1700.00);

