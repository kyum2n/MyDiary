INSERT INTO member (u_name, u_pwd, u_email, u_image) VALUES
('테스트유저', '$2a$10$zSAdH5aLDGkUT6f1UR7qgeYzHwfyM9Myc.XRFl0D8iWxnlfRMgIZ2', 'test@example.com', 'default.png');

INSERT INTO diary (u_id, title, date, location, content) VALUES
(1, '봄 소풍', '2025-04-29', '서울숲', '따뜻한 봄날에 산책을 다녀왔다.'),
(1, '비 오는 날', '2025-04-30', '신촌', '감성적인 하루였다.');