DROP TABLE IF EXISTS diary;
DROP TABLE IF EXISTS member;

-- 회원 테이블
CREATE TABLE member (
    uId varchar(100) NOT NULL PRIMARY KEY,
    uName varchar(100) NOT NULL,
    uPwd varchar(100) NOT NULL,
    uEmail varchar(100) NOT NULL,
    uImage VARCHAR(255),
    provider VARCHAR(100),
    providerId VARCHAR(100)
);

-- 일기 테이블
CREATE TABLE diary (
    id serial NOT NULL PRIMARY KEY,
    uId varchar(100) NOT NULL,
    title varchar(200) NOT NULL,
    date DATE NOT NULL,
    content text,
    image VARCHAR(255),
    location VARCHAR(100),

    FOREIGN KEY (uId) REFERENCES member (uId)
);