SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM authorities;
DELETE FROM user_authorities;
DELETE FROM users;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO authorities(authority_name, created_at, updated_at) VALUES('ROLE_USER', now(), now());
INSERT INTO authorities(authority_name, created_at, updated_at) VALUES('ROLE_ADMIN', now(), now());
INSERT INTO authorities(authority_name, created_at, updated_at) VALUES('ROLE_STORE_MANAGER', now(), now());
