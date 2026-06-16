CREATE TABLE IF NOT EXISTS clinics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    logo_url VARCHAR(255),
    category VARCHAR(100),
    city VARCHAR(100),
    address VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(100),
    rating DECIMAL(2,1),
    description VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
    );