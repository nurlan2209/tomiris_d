-- Create the 'users' table
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50)
);

-- Create the 'message' table
CREATE TABLE message (
                         id SERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         user_message TEXT,
                         bot_message TEXT,
                         FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
