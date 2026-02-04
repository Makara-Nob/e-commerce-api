-- 1. Insert Roles (Independent)
INSERT INTO roles (name, created_at, updated_at) VALUES 
('ADMIN', NOW(), NOW()),
('STAFF', NOW(), NOW()),
('CUSTOMER', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- 2. Insert Users
INSERT INTO users (username, email, password, full_name, position, status, user_permission, created_at, updated_at) VALUES 
('admin_user', 'admin@example.com', '$2a$10$wT5f...hashed', 'Super Administrator', 'Manager', 'ACTIVE', 'APPROVED', NOW(), NOW()),
('staff_user', 'staff@example.com', '$2a$10$wT5f...hashed', 'Staff Member', 'Sales', 'ACTIVE', 'APPROVED', NOW(), NOW()),
('customer_user', 'customer@example.com', '$2a$10$wT5f...hashed', 'John Doe', NULL, 'ACTIVE', 'NORMAL', NOW(), NOW())
ON CONFLICT (username) DO NOTHING;

-- Map Users to Roles
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'admin_user' AND r.name = 'ADMIN'
ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'staff_user' AND r.name = 'STAFF'
ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'customer_user' AND r.name = 'CUSTOMER'
ON CONFLICT DO NOTHING;

-- 3. Insert Suppliers (Using WHERE NOT EXISTS for idempotency if no unique constraint exists)
INSERT INTO suppliers (name, contact_person, email, phone, address, status, created_at, updated_at)
SELECT 'Global Tech Supplies', 'Alice Smith', 'alice@globaltech.com', '+123456789', '123 Tech Blvd, Silicon Valley', 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'Global Tech Supplies');

INSERT INTO suppliers (name, contact_person, email, phone, address, status, created_at, updated_at)
SELECT 'Fashi on Forward Inc', 'Bob Jones', 'bob@fashionforward.com', '+987654321', '456 Style Ave, New York', 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'Fashion Forward Inc');

INSERT INTO suppliers (name, contact_person, email, phone, address, status, created_at, updated_at)
SELECT 'Daily Essentials Co', 'Charlie Brown', 'charlie@daily.com', '+1122334455', '789 Grocery Ln, London', 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'Daily Essentials Co');

INSERT INTO suppliers (name, contact_person, email, phone, address, status, created_at, updated_at)
SELECT 'Sports Gear Pro', 'David Beckham', 'david@sports.com', '+1998877665', 'Stadium Rd, Manchester', 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'Sports Gear Pro');

INSERT INTO suppliers (name, contact_person, email, phone, address, status, created_at, updated_at)
SELECT 'Beauty & Glow', 'Eva Green', 'eva@beauty.com', '+1555666777', 'Champs-Élysées, Paris', 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'Beauty & Glow');

INSERT INTO suppliers (name, contact_person, email, phone, address, status, created_at, updated_at)
SELECT 'Book World', 'Frank Miller', 'frank@books.com', '+1444333222', 'Library St, Oxford', 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'Book World');

-- 4. Insert Brands (Has Unique Constraint on name)
INSERT INTO brands (name, description, logo_url, status, created_at, updated_at) VALUES 
('TechMaster', 'High quality electronics', 'https://example.com/techmaster.png', 'ACTIVE', NOW(), NOW()),
('StyleVista', 'Trendy clothing', 'https://example.com/stylevista.png', 'ACTIVE', NOW(), NOW()),
('HomeBasics', 'Everyday home items', 'https://example.com/homebasics.png', 'ACTIVE', NOW(), NOW()),
('Nike', 'Just Do It', 'https://example.com/nike.png', 'ACTIVE', NOW(), NOW()),
('Adidas', 'Impossible is Nothing', 'https://example.com/adidas.png', 'ACTIVE', NOW(), NOW()),
('Samsung', 'Inspire the World', 'https://example.com/samsung.png', 'ACTIVE', NOW(), NOW()),
('Apple', 'Think Different', 'https://example.com/apple.png', 'ACTIVE', NOW(), NOW()),
('Sony', 'Be Moved', 'https://example.com/sony.png', 'ACTIVE', NOW(), NOW()),
('Logitech', 'Defy Logic', 'https://example.com/logitech.png', 'ACTIVE', NOW(), NOW()),
('L''Oreal', 'Because you''re worth it', 'https://example.com/loreal.png', 'ACTIVE', NOW(), NOW())
ON CONFLICT (name) DO NOTHING;

-- 5. Insert Categories (Has Unique Constraint on code/name)
INSERT INTO categories (name, code, description, status, created_at, updated_at) VALUES 
('Electronics', 'ELEC', 'Gadgets and devices', 'ACTIVE', NOW(), NOW()),
('Clothing', 'CLOTH', 'Men and Women apparel', 'ACTIVE', NOW(), NOW()),
('Home & Garden', 'HOME', 'Furniture and decor', 'ACTIVE', NOW(), NOW()),
('Sports', 'SPORT', 'Sporting goods and equipment', 'ACTIVE', NOW(), NOW()),
('Beauty', 'BEAUTY', 'Cosmetics and personal care', 'ACTIVE', NOW(), NOW()),
('Books', 'BOOK', 'Education and entertainment', 'ACTIVE', NOW(), NOW()),
('Toys', 'TOYS', 'Kids toys and games', 'ACTIVE', NOW(), NOW())
ON CONFLICT (code) DO NOTHING;

-- 6. Insert Products (Has Unique Constraint on sku)
INSERT INTO products (name, sku, description, category_id, supplier_id, brand_id, quantity, min_stock, cost_price, selling_price, status, created_at, updated_at) VALUES 
-- Electronics
('Samsung Galaxy S23', 'SKU-ELEC-001', 'Latest android flagship', 
    (SELECT id FROM categories WHERE code = 'ELEC'), 
    (SELECT id FROM suppliers WHERE name = 'Global Tech Supplies'), 
    (SELECT id FROM brands WHERE name = 'Samsung'), 
    50, 5, 600.00, 999.99, 'ACTIVE', NOW(), NOW()),
('Iphone 14 Pro', 'SKU-ELEC-002', 'Apple latest smartphone', 
    (SELECT id FROM categories WHERE code = 'ELEC'), 
    (SELECT id FROM suppliers WHERE name = 'Global Tech Supplies'), 
    (SELECT id FROM brands WHERE name = 'Apple'), 
    40, 5, 800.00, 1199.99, 'ACTIVE', NOW(), NOW()),
('Sony WH-1000XM5', 'SKU-ELEC-003', 'Noise cancelling headphones', 
    (SELECT id FROM categories WHERE code = 'ELEC'), 
    (SELECT id FROM suppliers WHERE name = 'Global Tech Supplies'), 
    (SELECT id FROM brands WHERE name = 'Sony'), 
    30, 5, 200.00, 349.99, 'ACTIVE', NOW(), NOW()),
('MacBook Pro M2', 'SKU-ELEC-004', 'Powerful laptop for pros', 
    (SELECT id FROM categories WHERE code = 'ELEC'), 
    (SELECT id FROM suppliers WHERE name = 'Global Tech Supplies'), 
    (SELECT id FROM brands WHERE name = 'Apple'), 
    15, 2, 1500.00, 1999.99, 'ACTIVE', NOW(), NOW()),
('Logitech MX Master 3S', 'SKU-ELEC-005', 'Ergonomic mouse', 
    (SELECT id FROM categories WHERE code = 'ELEC'), 
    (SELECT id FROM suppliers WHERE name = 'Global Tech Supplies'), 
    (SELECT id FROM brands WHERE name = 'Logitech'), 
    60, 10, 50.00, 99.99, 'ACTIVE', NOW(), NOW()),
('Samsung 4K Monitor', 'SKU-ELEC-006', '32 inch UHD display', 
    (SELECT id FROM categories WHERE code = 'ELEC'), 
    (SELECT id FROM suppliers WHERE name = 'Global Tech Supplies'), 
    (SELECT id FROM brands WHERE name = 'Samsung'), 
    20, 3, 300.00, 499.99, 'ACTIVE', NOW(), NOW()),

-- Clothing
('Nike Air Max', 'SKU-CLOTH-001', 'Running shoes', 
    (SELECT id FROM categories WHERE code = 'CLOTH'), 
    (SELECT id FROM suppliers WHERE name = 'Fashion Forward Inc'), 
    (SELECT id FROM brands WHERE name = 'Nike'), 
    100, 20, 60.00, 129.99, 'ACTIVE', NOW(), NOW()),
('Adidas Ultraboost', 'SKU-CLOTH-002', 'Comfortable running sneakers', 
    (SELECT id FROM categories WHERE code = 'CLOTH'), 
    (SELECT id FROM suppliers WHERE name = 'Fashion Forward Inc'), 
    (SELECT id FROM brands WHERE name = 'Adidas'), 
    80, 15, 70.00, 159.99, 'ACTIVE', NOW(), NOW()),
('Nike Hoodie', 'SKU-CLOTH-003', 'Fleece pullover', 
    (SELECT id FROM categories WHERE code = 'CLOTH'), 
    (SELECT id FROM suppliers WHERE name = 'Fashion Forward Inc'), 
    (SELECT id FROM brands WHERE name = 'Nike'), 
    150, 30, 25.00, 59.99, 'ACTIVE', NOW(), NOW()),
('Adidas Track Pants', 'SKU-CLOTH-004', 'Athletic training pants', 
    (SELECT id FROM categories WHERE code = 'CLOTH'), 
    (SELECT id FROM suppliers WHERE name = 'Fashion Forward Inc'), 
    (SELECT id FROM brands WHERE name = 'Adidas'), 
    120, 25, 20.00, 49.99, 'ACTIVE', NOW(), NOW()),
('Summer Dress', 'SKU-CLOTH-005', 'Floral print dress', 
    (SELECT id FROM categories WHERE code = 'CLOTH'), 
    (SELECT id FROM suppliers WHERE name = 'Fashion Forward Inc'), 
    (SELECT id FROM brands WHERE name = 'StyleVista'), 
    50, 5, 15.00, 39.99, 'ACTIVE', NOW(), NOW()),
('Denim Jacket', 'SKU-CLOTH-006', 'Classic blue jean jacket', 
    (SELECT id FROM categories WHERE code = 'CLOTH'), 
    (SELECT id FROM suppliers WHERE name = 'Fashion Forward Inc'), 
    (SELECT id FROM brands WHERE name = 'StyleVista'), 
    40, 5, 25.00, 69.99, 'ACTIVE', NOW(), NOW()),

-- Home & Garden
('Coffee Table', 'SKU-HOME-001', 'Wooden coffee table', 
    (SELECT id FROM categories WHERE code = 'HOME'), 
    (SELECT id FROM suppliers WHERE name = 'Daily Essentials Co'), 
    (SELECT id FROM brands WHERE name = 'HomeBasics'), 
    20, 2, 80.00, 150.00, 'ACTIVE', NOW(), NOW()),
('Table Lamp', 'SKU-HOME-002', 'Modern desk lamp', 
    (SELECT id FROM categories WHERE code = 'HOME'), 
    (SELECT id FROM suppliers WHERE name = 'Daily Essentials Co'), 
    (SELECT id FROM brands WHERE name = 'HomeBasics'), 
    30, 5, 20.00, 45.99, 'ACTIVE', NOW(), NOW()),
('Sofa bed', 'SKU-HOME-003', 'Convertible sofa', 
    (SELECT id FROM categories WHERE code = 'HOME'), 
    (SELECT id FROM suppliers WHERE name = 'Daily Essentials Co'), 
    (SELECT id FROM brands WHERE name = 'HomeBasics'), 
    5, 1, 200.00, 499.00, 'ACTIVE', NOW(), NOW()),
('Kitchen Knife Set', 'SKU-HOME-004', 'Stainless steel knives', 
    (SELECT id FROM categories WHERE code = 'HOME'), 
    (SELECT id FROM suppliers WHERE name = 'Daily Essentials Co'), 
    (SELECT id FROM brands WHERE name = 'HomeBasics'), 
    40, 10, 30.00, 79.99, 'ACTIVE', NOW(), NOW()),

-- Sports
('Tennis Racket', 'SKU-SPORT-001', 'Pro level racket', 
    (SELECT id FROM categories WHERE code = 'SPORT'), 
    (SELECT id FROM suppliers WHERE name = 'Sports Gear Pro'), 
    (SELECT id FROM brands WHERE name = 'Nike'), 
    25, 5, 50.00, 120.00, 'ACTIVE', NOW(), NOW()),
('Basketball', 'SKU-SPORT-002', 'Official size 7', 
    (SELECT id FROM categories WHERE code = 'SPORT'), 
    (SELECT id FROM suppliers WHERE name = 'Sports Gear Pro'), 
    (SELECT id FROM brands WHERE name = 'Nike'), 
    100, 10, 10.00, 29.99, 'ACTIVE', NOW(), NOW()),
('Yoga Mat', 'SKU-SPORT-003', 'Non-slip mat', 
    (SELECT id FROM categories WHERE code = 'SPORT'), 
    (SELECT id FROM suppliers WHERE name = 'Sports Gear Pro'), 
    (SELECT id FROM brands WHERE name = 'Adidas'), 
    80, 10, 15.00, 35.00, 'ACTIVE', NOW(), NOW()),
('Dumbbell Set', 'SKU-SPORT-004', 'Adjustable weights', 
    (SELECT id FROM categories WHERE code = 'SPORT'), 
    (SELECT id FROM suppliers WHERE name = 'Sports Gear Pro'), 
    (SELECT id FROM brands WHERE name = 'HomeBasics'), 
    20, 2, 80.00, 199.99, 'ACTIVE', NOW(), NOW()),

-- Beauty
('Facial Cream', 'SKU-BEAUTY-001', 'Anti-aging moisturizer', 
    (SELECT id FROM categories WHERE code = 'BEAUTY'), 
    (SELECT id FROM suppliers WHERE name = 'Beauty & Glow'), 
    (SELECT id FROM brands WHERE name = 'L''Oreal'), 
    200, 20, 10.00, 29.99, 'ACTIVE', NOW(), NOW()),
('Lipstick Red', 'SKU-BEAUTY-002', 'Matte finish lipstick', 
    (SELECT id FROM categories WHERE code = 'BEAUTY'), 
    (SELECT id FROM suppliers WHERE name = 'Beauty & Glow'), 
    (SELECT id FROM brands WHERE name = 'L''Oreal'), 
    300, 50, 5.00, 15.99, 'ACTIVE', NOW(), NOW()),
('Shampoo', 'SKU-BEAUTY-003', 'For dry hair', 
    (SELECT id FROM categories WHERE code = 'BEAUTY'), 
    (SELECT id FROM suppliers WHERE name = 'Beauty & Glow'), 
    (SELECT id FROM brands WHERE name = 'L''Oreal'), 
    150, 20, 4.00, 9.99, 'ACTIVE', NOW(), NOW())
ON CONFLICT (sku) DO NOTHING;

-- 7. Insert Banners
INSERT INTO banners (title, image_url, link_url, description, display_order, status, created_at, updated_at)
SELECT 'Summer Sale', 'https://example.com/summer-sale.jpg', '/products?category=summer', 'Up to 50% off on Summer items', 1, 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM banners WHERE title = 'Summer Sale');

INSERT INTO banners (title, image_url, link_url, description, display_order, status, created_at, updated_at)
SELECT 'New Arrivals', 'https://example.com/new-arrivals.jpg', '/products?sort=new', 'Check out the latest gadgets', 2, 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM banners WHERE title = 'New Arrivals');

INSERT INTO banners (title, image_url, link_url, description, display_order, status, created_at, updated_at)
SELECT 'Back to School', 'https://example.com/back-to-school.jpg', '/products?category=books', 'Essentials for students', 3, 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM banners WHERE title = 'Back to School');

INSERT INTO banners (title, image_url, link_url, description, display_order, status, created_at, updated_at)
SELECT 'Black Friday', 'https://example.com/black-friday.jpg', '/products', 'Biggest sale of the year', 4, 'ACTIVE', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM banners WHERE title = 'Black Friday');

-- 8. Insert Stock Transactions
INSERT INTO stock_transactions (product_id, type, quantity, previous_stock, new_stock, reference, notes, transaction_date, created_at, updated_at)
SELECT p.id, 'STOCK_IN', 50, 0, 50, 'PO-MASS-001', 'Initial stock', NOW(), NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-001'
AND NOT EXISTS (SELECT 1 FROM stock_transactions WHERE reference = 'PO-MASS-001');

INSERT INTO stock_transactions (product_id, type, quantity, previous_stock, new_stock, reference, notes, transaction_date, created_at, updated_at)
SELECT p.id, 'STOCK_IN', 100, 0, 100, 'PO-MASS-002', 'Initial stock', NOW(), NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-CLOTH-001'
AND NOT EXISTS (SELECT 1 FROM stock_transactions WHERE reference = 'PO-MASS-002');

-- 9. Insert Orders (Has Unique Constraint on invoice_number)
INSERT INTO orders (invoice_number, user_id, total_amount, discount_amount, net_amount, status, payment_method, shipping_address, note, created_at, updated_at) VALUES 
('INV-2023001', (SELECT id FROM users WHERE username = 'customer_user'), 999.99, 0.00, 999.99, 'PENDING', 'CASH', '123 Main St, City', 'Call before delivery', NOW(), NOW()),
('INV-2023002', (SELECT id FROM users WHERE username = 'customer_user'), 159.99, 0.00, 159.99, 'CONFIRMED', 'CARD', '123 Main St, City', '', NOW(), NOW())
ON CONFLICT (invoice_number) DO NOTHING;

-- 10. Insert Order Items (Safe to insert if Order exists, checking uniqueness by order_id and product_id conceptually)
INSERT INTO order_items (order_id, product_id, quantity, unit_price, sub_total, created_at, updated_at)
SELECT o.id, p.id, 1, 999.99, 999.99, NOW(), NOW()
FROM orders o, products p
WHERE o.invoice_number = 'INV-2023001' AND p.sku = 'SKU-ELEC-001'
AND NOT EXISTS (SELECT 1 FROM order_items WHERE order_id = o.id AND product_id = p.id);

INSERT INTO order_items (order_id, product_id, quantity, unit_price, sub_total, created_at, updated_at)
SELECT o.id, p.id, 1, 159.99, 159.99, NOW(), NOW()
FROM orders o, products p
WHERE o.invoice_number = 'INV-2023002' AND p.sku = 'SKU-CLOTH-002'
AND NOT EXISTS (SELECT 1 FROM order_items WHERE order_id = o.id AND product_id = p.id);

-- 11. Insert Product Variants (Examples for Electronics and Clothing)
-- iPhone variants (different storage and colors)
INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '128GB - Black', 'SKU-ELEC-002-128-BLK', '128GB', 'Black', 15, 0.00, 'https://example.com/iphone-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-002'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-002-128-BLK');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '256GB - Black', 'SKU-ELEC-002-256-BLK', '256GB', 'Black', 12, 100.00, 'https://example.com/iphone-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-002'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-002-256-BLK');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '256GB - Silver', 'SKU-ELEC-002-256-SLV', '256GB', 'Silver', 8, 100.00, 'https://example.com/iphone-silver.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-002'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-002-256-SLV');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '512GB - Black', 'SKU-ELEC-002-512-BLK', '512GB', 'Black', 5, 200.00, 'https://example.com/iphone-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-002'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-002-512-BLK');

-- Samsung Galaxy variants
INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '128GB - Phantom Black', 'SKU-ELEC-001-128-BLK', '128GB', 'Phantom Black', 20, 0.00, 'https://example.com/galaxy-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-001'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-001-128-BLK');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '256GB - Phantom Black', 'SKU-ELEC-001-256-BLK', '256GB', 'Phantom Black', 15, 50.00, 'https://example.com/galaxy-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-001'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-001-256-BLK');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '256GB - Cream', 'SKU-ELEC-001-256-CRM', '256GB', 'Cream', 15, 50.00, 'https://example.com/galaxy-cream.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-001'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-001-256-CRM');

-- MacBook Pro variants
INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '16GB RAM - 512GB SSD', 'SKU-ELEC-004-16-512', '512GB', 'Space Gray', 7, 0.00, 'https://example.com/macbook-gray.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-004'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-004-16-512');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '32GB RAM - 1TB SSD', 'SKU-ELEC-004-32-1TB', '1TB', 'Space Gray', 5, 400.00, 'https://example.com/macbook-gray.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-004'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-004-32-1TB');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, '32GB RAM - 1TB SSD - Silver', 'SKU-ELEC-004-32-1TB-SLV', '1TB', 'Silver', 3, 400.00, 'https://example.com/macbook-silver.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-004'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-ELEC-004-32-1TB-SLV');

-- Nike Air Max variants (shoe sizes)
INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, 'Size 8 - Black/White', 'SKU-CLOTH-001-8-BLK', 'US 8', 'Black/White', 20, 0.00, 'https://example.com/airmax-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-CLOTH-001'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-CLOTH-001-8-BLK');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, 'Size 9 - Black/White', 'SKU-CLOTH-001-9-BLK', 'US 9', 'Black/White', 25, 0.00, 'https://example.com/airmax-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-CLOTH-001'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-CLOTH-001-9-BLK');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, 'Size 10 - Black/White', 'SKU-CLOTH-001-10-BLK', 'US 10', 'Black/White', 30, 0.00, 'https://example.com/airmax-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-CLOTH-001'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-CLOTH-001-10-BLK');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, 'Size 10 - Red/White', 'SKU-CLOTH-001-10-RED', 'US 10', 'Red/White', 15, 10.00, 'https://example.com/airmax-red.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-CLOTH-001'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-CLOTH-001-10-RED');

INSERT INTO product_variants (product_id, variant_name, sku, size, color, stock_quantity, additional_price, image_url, status, created_at, updated_at)
SELECT p.id, 'Size 11 - Black/White', 'SKU-CLOTH-001-11-BLK', 'US 11', 'Black/White', 10, 0.00, 'https://example.com/airmax-black.jpg', 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-CLOTH-001'
AND NOT EXISTS (SELECT 1 FROM product_variants WHERE sku = 'SKU-CLOTH-001-11-BLK');

-- 12. Insert Promotions
-- Black Friday sale on iPhone
INSERT INTO promotions (name, description, discount_type, discount_value, start_date, end_date, product_id, status, created_at, updated_at)
SELECT 'iPhone Black Friday Sale', 'Special discount on iPhone 14 Pro', 'PERCENTAGE', 15.00, 
       NOW() - INTERVAL '5 days', NOW() + INTERVAL '25 days', p.id, 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-002'
AND NOT EXISTS (SELECT 1 FROM promotions WHERE name = 'iPhone Black Friday Sale');

-- Samsung Galaxy promotion
INSERT INTO promotions (name, description, discount_type, discount_value, start_date, end_date, product_id, status, created_at, updated_at)
SELECT 'Galaxy S23 Launch Offer', 'Limited time discount on Galaxy S23', 'FIXED_AMOUNT', 100.00, 
       NOW(), NOW() + INTERVAL '30 days', p.id, 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-001'
AND NOT EXISTS (SELECT 1 FROM promotions WHERE name = 'Galaxy S23 Launch Offer');

-- Nike shoes promotion
INSERT INTO promotions (name, description, discount_type, discount_value, start_date, end_date, product_id, status, created_at, updated_at)
SELECT 'Nike Air Max Deal', 'Summer sports sale on Nike Air Max', 'PERCENTAGE', 20.00, 
       NOW() - INTERVAL '10 days', NOW() + INTERVAL '20 days', p.id, 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-CLOTH-001'
AND NOT EXISTS (SELECT 1 FROM promotions WHERE name = 'Nike Air Max Deal');

-- MacBook Pro promotion
INSERT INTO promotions (name, description, discount_type, discount_value, start_date, end_date, product_id, status, created_at, updated_at)
SELECT 'Back to School MacBook', 'Student discount on MacBook Pro', 'FIXED_AMOUNT', 200.00, 
       NOW(), NOW() + INTERVAL '60 days', p.id, 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-004'
AND NOT EXISTS (SELECT 1 FROM promotions WHERE name = 'Back to School MacBook');

-- Sony headphones promotion
INSERT INTO promotions (name, description, discount_type, discount_value, start_date, end_date, product_id, status, created_at, updated_at)
SELECT 'Sony Headphones Flash Sale', '10% off on WH-1000XM5', 'PERCENTAGE', 10.00, 
       NOW() + INTERVAL '5 days', NOW() + INTERVAL '15 days', p.id, 'ACTIVE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-ELEC-003'
AND NOT EXISTS (SELECT 1 FROM promotions WHERE name = 'Sony Headphones Flash Sale');

-- Past promotion example (soft deleted)
INSERT INTO promotions (name, description, discount_type, discount_value, start_date, end_date, product_id, status, created_at, updated_at)
SELECT 'Summer Hoodie Sale', 'Expired summer clearance', 'PERCENTAGE', 25.00, 
       NOW() - INTERVAL '60 days', NOW() - INTERVAL '30 days', p.id, 'DELETE', NOW(), NOW()
FROM products p WHERE p.sku = 'SKU-CLOTH-003'
AND NOT EXISTS (SELECT 1 FROM promotions WHERE name = 'Summer Hoodie Sale');
