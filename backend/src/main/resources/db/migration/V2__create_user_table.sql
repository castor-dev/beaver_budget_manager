CREATE TABLE users (
    id                      SERIAL PRIMARY KEY,
    username                VARCHAR(100) NOT NULL UNIQUE,
    email                   VARCHAR(150),
    password_hash           VARCHAR(255) NOT NULL,
    password_expire_date    TIMESTAMP,
    CONSTRAINT uq_users_email UNIQUE (email)
);