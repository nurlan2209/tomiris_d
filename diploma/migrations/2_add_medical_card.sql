-- Create the 'medical_card' table
CREATE TABLE medical_card (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    birth_date DATE,
    gender VARCHAR(20),
    height INTEGER,
    weight INTEGER,
    blood_type VARCHAR(10),
    allergies TEXT,
    chronic_diseases TEXT,
    current_medications TEXT,
    medical_history TEXT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);