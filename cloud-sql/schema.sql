CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    password VARCHAR(80) NOT NULL,
    role VARCHAR(30) NOT NULL,
    purchases INT NOT NULL DEFAULT 0,
    penalties INT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY,
    name VARCHAR(160) NOT NULL,
    category VARCHAR(80) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    image_url TEXT,
    seller_email VARCHAR(160) NOT NULL,
    approved BOOLEAN NOT NULL DEFAULT FALSE,
    rating_average DECIMAL(2,1) NOT NULL DEFAULT 5.0,
    review_count INT NOT NULL DEFAULT 0
);

CREATE TABLE reviews (
    id BIGINT PRIMARY KEY,
    product_name VARCHAR(160) NOT NULL,
    buyer_email VARCHAR(160) NOT NULL,
    seller_email VARCHAR(160) NOT NULL,
    rating INT NOT NULL,
    condition_text VARCHAR(160) NOT NULL,
    comment TEXT NOT NULL
);
