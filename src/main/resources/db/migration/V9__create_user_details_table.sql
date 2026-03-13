CREATE TABLE user_details (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT NOT NULL,
    first_name NVARCHAR(80) NULL,
    last_name NVARCHAR(80) NULL,
    phone NVARCHAR(40) NULL,
    dni NVARCHAR(40) NULL,
    birth_date DATE NULL,
    address_line NVARCHAR(160) NULL,
    address_number NVARCHAR(20) NULL,
    floor NVARCHAR(20) NULL,
    apartment NVARCHAR(20) NULL,
    city NVARCHAR(120) NULL,
    state NVARCHAR(120) NULL,
    postal_code NVARCHAR(20) NULL,
    country_code NVARCHAR(2) NULL,
    created_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    updated_at DATETIMEOFFSET NOT NULL DEFAULT SYSDATETIMEOFFSET(),
    CONSTRAINT fk_user_details_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT uq_user_details_user UNIQUE (user_id),
    CONSTRAINT ck_user_details_country_code_len CHECK (country_code IS NULL OR LEN(country_code) = 2)
);
GO

CREATE INDEX idx_user_details_user_id ON user_details(user_id);
