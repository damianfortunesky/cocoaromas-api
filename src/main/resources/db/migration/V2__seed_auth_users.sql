INSERT INTO users (name, email, username, password_hash, role)
VALUES
('Admin User', 'admin@cocoaromas.local', 'admin', '{noop}Admin123!', 'ADMIN'),
('Owner User', 'owner@cocoaromas.local', 'owner', '{noop}Owner123!', 'OWNER'),
('Employee User', 'employee@cocoaromas.local', 'employee', '{noop}Employee123!', 'EMPLOYEE'),
('Client User', 'client@cocoaromas.local', 'client', '{noop}Client123!', 'CLIENT');
