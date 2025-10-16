CREATE TABLE accounts (
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    owner_id        INTEGER NOT NULL,
    balance         NUMERIC(15, 2) DEFAULT 0.00,
    currency        VARCHAR(10),

    CONSTRAINT fk_account_user
        FOREIGN KEY (owner_id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_accounts_owner_id ON accounts(owner_id);
