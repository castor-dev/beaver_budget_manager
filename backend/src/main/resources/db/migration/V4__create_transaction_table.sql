CREATE TABLE transactions (
    id                  SERIAL PRIMARY KEY,
    account_id          INTEGER NOT NULL,
    amount              NUMERIC(15, 2) NOT NULL,
    type                VARCHAR(20) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')),
    date                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description         VARCHAR(255),
    planned             BOOLEAN DEFAULT FALSE,
    parent_transaction_id  INTEGER NULL,

    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id) REFERENCES accounts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_transaction_parent
        FOREIGN KEY (parent_transaction_id) REFERENCES transactions(id)
        ON DELETE SET NULL
);