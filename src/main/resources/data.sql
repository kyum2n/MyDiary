INSERT INTO member (uId, uName, uPwd, uEmail, uImage, provider, provider_id) VALUES
('test001',
 '테스트유저',
 '$2a$10$Wd/XVl7ahENl/HlCBtfBjeyLbq2/Os884Y7CjvKqjE5j0YQoXoq6O',  -- 비밀번호: test1234
 'test@example.com',
 'default.png',
 NULL,
 NULL
);

-- 해당 사용자에 대한 샘플 일기 데이터 등록 = 변경 가능함
INSERT INTO diary (uId, title, date, image, location, content) VALUES
('test001', '봄 소풍', '2025-04-29', 'spring.jpg', '서울숲', '따뜻한 봄날에 산책을 다녀왔다.'),
('test001', '비 오는 날', '2025-04-30', 'rain.jpg', '신촌', '감성적인 하루였다.');