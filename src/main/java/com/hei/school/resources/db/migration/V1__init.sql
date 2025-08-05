CREATE TABLE donation (
                          id UUID PRIMARY KEY,
                          donor_name TEXT NOT NULL,
                          donor_email TEXT NOT NULL,
                          payment_reference TEXT NOT NULL,
                          amount DOUBLE PRECISION,
                          payment_method TEXT,
                          payment_date DATE,
                          status TEXT CHECK (status IN ('VERIFYING', 'SUCCEEDED', 'FAILED'))
);

CREATE TABLE help (
                      id UUID PRIMARY KEY,
                      beneficiary_name TEXT NOT NULL,
                      beneficiary_email TEXT NOT NULL,
                      payment_reference TEXT NOT NULL,
                      amount DOUBLE PRECISION,
                      payment_method TEXT,
                      payment_date DATE,
                      status TEXT CHECK (status IN ('SUCCEEDED', 'FAILED')),
                      description TEXT
);

CREATE EXTENSION IF NOT EXISTS "pgcrypto";
