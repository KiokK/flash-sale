CREATE TABLE outbox (
    id           UUID        PRIMARY KEY,             -- = eventId
    aggregate_id TEXT        NOT NULL,
    topic        TEXT        NOT NULL,
    key          TEXT        NOT NULL,
    type         TEXT        NOT NULL,
    payload      JSONB       NOT NULL,
    headers      JSONB       NOT NULL DEFAULT '{}'::jsonb,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now(),
    sent_at      TIMESTAMPTZ
);

-- Polling publisher: WHERE sent_at IS NULL ORDER BY created_at ... FOR UPDATE SKIP LOCKED
CREATE INDEX idx_outbox_unsent ON outbox (created_at) WHERE sent_at IS NULL;

-- to clear sent messages by retention.
-- CREATE INDEX idx_outbox_sent_at ON outbox (sent_at) WHERE sent_at IS NOT NULL;

-- Inbox: INSERT ... ON CONFLICT DO NOTHING in THE transaction with action
CREATE TABLE processed_events (
    consumer     TEXT        NOT NULL,
    event_id     UUID        NOT NULL,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (consumer, event_id)
);

-- CREATE INDEX idx_processed_events_processed_at ON processed_events (processed_at);
