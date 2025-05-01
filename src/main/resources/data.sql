INSERT INTO member (u_name, u_pwd, u_email, u_image) VALUES
('테스트유저', 
'$2a$20$nicuIEiVK4YY5bE.PUmddOq7Bz5OLmzV78s6bdj9jkoN2WzlQcUUS', --비밀번호 : test1234 
'test@example.com', --아이디 
'default.png'
);

INSERT INTO diary (u_id, title, date, location, content) VALUES
(1, '봄 소풍', '2025-04-29', '서울숲', '따뜻한 봄날에 산책을 다녀왔다.'),
(1, '비 오는 날', '2025-04-30', '신촌', '감성적인 하루였다.');