DROP TABLE IF EXISTS diary;
DROP TABLE IF EXISTS member;

CREATE TABLE IF NOT EXISTS member (
    uId VARCHAR(100) NOT NULL PRIMARY KEY,
    uName VARCHAR(100) NOT NULL,
    uPwd VARCHAR(100) NOT NULL,
    uEmail VARCHAR(100) NOT NULL UNIQUE,
    uImage VARCHAR(100),
    provider VARCHAR(100),           -- ✅ 소셜 로그인 제공자
    provider_id VARCHAR(100)         -- ✅ 소셜 사용자 고유 ID
);

CREATE TABLE IF NOT EXISTS diary (
    id SERIAL NOT NULL PRIMARY KEY,
    uId VARCHAR(100) NOT NULL,
    title VARCHAR(200) NOT NULL,
    date DATE NOT NULL,
    image VARCHAR(200),
    location VARCHAR(100),
    content TEXT,
    CONSTRAINT fk_member FOREIGN KEY (uId) REFERENCES member(uId) ON DELETE CASCADE
);