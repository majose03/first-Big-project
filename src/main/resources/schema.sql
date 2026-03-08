-- =============================================
--   BakeryHub - Database Bootstrap Script
--   Run this in MySQL before starting the app
-- =============================================

CREATE DATABASE IF NOT EXISTS bakery_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE bakery_db;

-- Hibernate will auto-create the tables via ddl-auto=update
-- This script just ensures the DB and a seed admin user exist.

-- After first run, you can insert admin user manually:
-- INSERT INTO users (username, email, password, role, enabled)
-- VALUES ('admin', 'admin@bakeryhub.com',
--   '$2a$12$KIXgBa4J9cTlNUoQpRyeZOoqVMvvB7gxH2kZhC.mY.rKp6zNjb/C6', 'ROLE_ADMIN', true);
-- (password = "admin123" bcrypt encoded)
