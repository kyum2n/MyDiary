CREATE TABLE member (
    u_id SERIAL PRIMARY KEY,
    u_name VARCHAR(100) NOT NULL,
    u_pwd VARCHAR(100) NOT NULL,
    u_email VARCHAR(100) NOT NULL UNIQUE,
    u_image VARCHAR(2200)
);

CREATE TABLE diary (
    id SERIAL PRIMARY KEY,
    u_id INTEGER NOT NULL,
    title VARCHAR(100),
    date DATE,
    location VARCHAR(100),
    content TEXT,
    CONSTRAINT fk_member FOREIGN KEY (u_id) REFERENCES member(u_id) ON DELETE CASCADE
);