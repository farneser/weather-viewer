CREATE TABLE Sessions (
    id UUID PRIMARY KEY DEFAULT public.uuid_generate_v4(),
    UserId UUID REFERENCES Users (id),
    expiresAt TIMESTAMP NOT NULL
);
