CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(200) NOT NULL,
                       email VARCHAR(200) UNIQUE NOT NULL,
                       phone VARCHAR(50),
                       role VARCHAR(50) NOT NULL DEFAULT 'USER',
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
