INSERT INTO users (username, email, password)
VALUES('John', 'john_doe@mail.com', '$2a$12$Jk99WkyuUgt52ZVHqGK6ceeXf7LiCvYWw7u91lqIcgy.lqMqy30Kq'),
('Jane', 'jane_doe@mail.com', '$2a$12$Jk99WkyuUgt52ZVHqGK6ceeXf7LiCvYWw7u91lqIcgy.lqMqy30Kq'),
('Jimmie', 'jimmie_doe@mail.com', '$2a$12$Jk99WkyuUgt52ZVHqGK6ceeXf7LiCvYWw7u91lqIcgy.lqMqy30Kq');

INSERT INTO account (balance, user_id)
VALUES(1000.00, 1),
(20.00, 2);

INSERT INTO user_connection(user_id, connected_user_id)
VALUES (1,2);