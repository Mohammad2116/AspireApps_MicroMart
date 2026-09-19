CREATE TYPE user_role as ENUM ('USER','ADMIN', 'SELLER');

CREATE TABLE users(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid() ,
    username VARCHAR(255) NOT NULL UNIQUE ,
    password VARCHAR(255) NOT NULL ,
    email VARCHAR(255) NOT NULL UNIQUE ,
    role user_role NOT NULL DEFAULT 'USER',
    create_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    deleted_at TIMESTAMPTZ
);

CREATE INDEX idx_users_username_active ON users (username) WHERE deleted_at IS NULL;
CREATE INDEX idx_users_email_active ON users (email) WHERE deleted_at IS NULL;