-- insert into member (uName, uId, uPwd, uEmail) values ('이규민', 'user1', '1234', 'aaa@naver.com');
INSERT INTO member (uId, uName, uPwd, uEmail, uImage, provider, providerId) VALUES
('test001',
'테스트유저',
'$2a$10$Wd/XVl7ahENl/HlCBtfBjeyLbq2/Os884Y7CjvKqjE5j0YQoXoq6O',  -- 비밀번호: test1234
'test@example.com',
'',
NULL,
NULL
);


-- INSERT into diary (uId, title, date, content, image, location)
--     VALUES ('user1', '첫 번째 일기', '2025-05-02', '이것은 첫 번째 일기입니다.', '', '서울');
INSERT INTO diary (uId, title, date, weather, image, location, content) VALUES
('test001', '봄 소풍', '2025-04-29', '', '', '서울숲', '따뜻한 봄날에 산책을 다녀왔다.');