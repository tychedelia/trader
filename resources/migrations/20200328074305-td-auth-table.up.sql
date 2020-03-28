CREATE TABLE IF NOT EXISTS td_auth
(
    id         SERIAL PRIMARY KEY,
    token_type VARCHAR(30) UNIQUE NOT NULL,
    token      TEXT NOT NULL ,
    expiry     TIMESTAMP,
    CHECK (token_type IN ('refresh_token', 'access_token'))
);