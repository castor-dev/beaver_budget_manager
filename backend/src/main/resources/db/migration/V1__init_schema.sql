-- =========================================
-- V1__init_schema.sql
-- =========================================

-- 1. Create the custom schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS beaver_budget AUTHORIZATION beaver_user;

-- 2. Switch to that schema
SET search_path TO beaver_budget;