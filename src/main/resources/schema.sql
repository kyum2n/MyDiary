CREATE TABLE member (
    u_id SERIAL PRIMARY KEY,
    u_name VARCHAR(50) NOT NULL,
    u_pwd VARCHAR(255) NOT NULL,
    u_email VARCHAR(100) NOT NULL UNIQUE,
    u_image VARCHAR(255)
);
--멤버 테이블