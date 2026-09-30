-- Bảng products cho Shopping Cart MVC JSP/Servlet (Hình 1-4)
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    imgurl LONGTEXT
);

INSERT INTO products (model, description, quantity, price, imgurl) VALUES
('Samsung', 'Samsung Smart TV UHD 4K Display', 10, 20000.0, 'images/samsung.png'),
('Sony', 'Sony Bravia 4K HDR Google TV', 10, 23000.0, 'images/sony.png'),
('Iphone 15', 'Apple iPhone 15 Titanium 128GB', 10, 17000.0, 'images/iphone15.png'),
('Iphone 16', 'Apple iPhone 16 Pro Max 256GB', 10, 18000.0, 'images/iphone16.png');

-- Bảng Product cho REST API (Tài liệu PDF)
CREATE TABLE IF NOT EXISTS Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    description TEXT,
    image_url VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS ShoppingCart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO Product (name, price, description, image_url) VALUES
('iPhone 15 Pro', 999.0, 'Titanium design, A17 Pro chip', 'https://picsum.photos/id/1/400/400'),
('Samsung Galaxy S24', 850.0, 'Galaxy AI, Dynamic AMOLED', 'https://picsum.photos/id/2/400/400'),
('MacBook Air M2', 1199.0, '13.6-inch Liquid Retina Display', 'https://picsum.photos/id/3/400/400'),
('Dell XPS 13', 1050.0, 'Intel Core i7 13th Gen, FHD+', 'https://picsum.photos/id/4/400/400'),
('Sony WH-1000XM5', 399.0, 'Noise Canceling Headphones', 'https://picsum.photos/id/5/400/400'),
('iPad Air 5', 599.0, 'Apple M1 chip, 10.9-inch display', 'https://picsum.photos/id/6/400/400'),
('Logitech MX Master 3S', 99.0, 'Wireless Performance Mouse', 'https://picsum.photos/id/7/400/400'),
('Keychron K2 V2', 79.0, 'Wireless Mechanical Keyboard', 'https://picsum.photos/id/8/400/400'),
('Apple Watch Series 9', 399.0, 'S9 SiP chip, Always-On Retina', 'https://picsum.photos/id/9/400/400'),
('LG UltraFine 4K', 450.0, '27-inch IPS UHD Display', 'https://picsum.photos/id/10/400/400');

INSERT INTO ShoppingCart (product_id, customer_name, quantity) VALUES
(1, 'Nguyen Van A', 1), (2, 'Tran Thi B', 2), (3, 'Le Van C', 1), (4, 'Pham Thi D', 1),
(5, 'Hoang Van E', 3), (6, 'Nguyen Thi F', 1), (7, 'Vu Van G', 2), (8, 'Dang Thi H', 4),
(9, 'Bui Van I', 1), (10, 'Do Thi K', 2), (1, 'Le Van L', 2), (3, 'Tran Thi M', 1),
(5, 'Nguyen Van N', 1), (2, 'Pham Van O', 3), (4, 'Bui Thi P', 1), (7, 'Hoang Thi Q', 2),
(8, 'Do Van R', 1), (6, 'Nguyen Van S', 2), (10, 'Tran Van T', 1), (9, 'Le Thi U', 1);
