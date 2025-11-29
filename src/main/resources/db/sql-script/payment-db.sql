CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    method VARCHAR(50) NOT NULL,
    amount NUMERIC(18,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    status VARCHAR(50) NOT NULL,
    message TEXT,
    processed_at TIMESTAMP WITH TIME ZONE,
    payload TEXT
);

CREATE INDEX IF NOT EXISTS idx_payments_method ON payments(method);