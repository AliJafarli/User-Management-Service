CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(200) NOT NULL,
                       email VARCHAR(200) UNIQUE NOT NULL,
                       role VARCHAR(50) NOT NULL DEFAULT 'USER',
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
