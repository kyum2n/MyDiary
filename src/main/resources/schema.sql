DROP TABLE IF EXISTS diary;
DROP TABLE IF EXISTS member;

CREATE TABLE IF NOT EXISTS member (
    uId VARCHAR(100) NOT NULL PRIMARY KEY,         -- 사용자 고유 ID
    uName VARCHAR(100) NOT NULL,                   -- 사용자 이름
    uPwd VARCHAR(100) NOT NULL,                    -- 암호화된 비밀번호
    uEmail VARCHAR(100) NOT NULL UNIQUE,           -- 이메일 (중복 불가)
    uImage VARCHAR(100),                           -- 프로필 이미지 파일명
    provider VARCHAR(100),                         -- 소셜 로그인 제공자 (예: naver, google)
    provider_id VARCHAR(100)                       -- 소셜 사용자 ID
);

-- 일기 정보 테이블 = 변경가능
CREATE TABLE IF NOT EXISTS diary (
    id SERIAL NOT NULL PRIMARY KEY,                -- 일기 고유 ID
    uId VARCHAR(100) NOT NULL,                     -- 작성자 ID (member 테이블 참조)
    title VARCHAR(200) NOT NULL,                   -- 일기 제목
    date DATE NOT NULL,                            -- 작성 날짜
    image VARCHAR(200),                            -- 첨부 이미지 파일명
    location VARCHAR(100),                         -- 작성 위치
    content TEXT,                                  -- 일기 내용
    CONSTRAINT fk_member 
        FOREIGN KEY (uId) REFERENCES member(uId) 
        ON DELETE CASCADE                          -- 사용자 삭제 시 일기도 함께 삭제
);