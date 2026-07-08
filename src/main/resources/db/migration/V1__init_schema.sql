CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       dob DATE NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       phone_number VARCHAR(255) NOT NULL,
                       kyc_status VARCHAR(50) NOT NULL,
                       created_at TIMESTAMP,
                       role VARCHAR(50)
);

CREATE TABLE account (
                         id SERIAL PRIMARY KEY,
                         user_id INTEGER REFERENCES users(id),
                         account_number VARCHAR(255),
                         account_type VARCHAR(50),
                         balance NUMERIC(38, 2)
);


CREATE TABLE transaction (
                             id SERIAL PRIMARY KEY,
                             sender_id INTEGER REFERENCES account(id),
                             receiver_id INTEGER REFERENCES account(id),
                             amount NUMERIC(38, 2) NOT NULL,
                             transaction_type VARCHAR(50),
                             fee NUMERIC(38, 2),
                             created_at TIMESTAMP,
                             transaction_status VARCHAR(50)
);

CREATE TABLE kyc_document (
                              id SERIAL PRIMARY KEY,
                              user_id INTEGER REFERENCES users(id),
                              status VARCHAR(50),
                              rejection_message VARCHAR(255)
);
