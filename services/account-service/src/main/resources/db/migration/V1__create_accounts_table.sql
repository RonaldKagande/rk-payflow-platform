CREATE TABLE accounts
(
    id UUID PRIMARY KEY,

    account_number VARCHAR(20) NOT NULL UNIQUE,

    customer_name VARCHAR(150) NOT NULL,

    email VARCHAR(255) NOT NULL,

    balance NUMERIC(19,2) NOT NULL,

    currency VARCHAR(3) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT chk_accounts_balance
        CHECK (balance >= 0),

    CONSTRAINT chk_accounts_status
        CHECK (status IN ('ACTIVE', 'FROZEN', 'CLOSED'))
);

CREATE INDEX idx_accounts_email
    ON accounts(email);