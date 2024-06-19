INSERT INTO users (username, email, password, role)
VALUES
('John', 'john_doe@mail.com', '$2a$12$pCN9Ly1k45REvCU2Qbbly.7.yOHLtD8KDjDAcQLNhyv.5QZeOvusi', 'USER'),
('Jane', 'jane_doe@mail.com', '$2a$12$pCN9Ly1k45REvCU2Qbbly.7.yOHLtD8KDjDAcQLNhyv.5QZeOvusi', 'USER'),
('Jimmie', 'jimmie_doe@mail.com', '$2a$12$pCN9Ly1k45REvCU2Qbbly.7.yOHLtD8KDjDAcQLNhyv.5QZeOvusi', 'USER'),
('Jennie', 'jennie_doe@mail.com', '$2a$12$pCN9Ly1k45REvCU2Qbbly.7.yOHLtD8KDjDAcQLNhyv.5QZeOvusi', 'USER'),
('admin', 'admin@paymybuddy.com', '$2a$12$pCN9Ly1k45REvCU2Qbbly.7.yOHLtD8KDjDAcQLNhyv.5QZeOvusi', 'ADMIN');

INSERT INTO accounts (balance, user_id)
VALUES
(1000.00, 1),
(100.00, 2),
(10.00, 3);

INSERT INTO user_connections (user_id, connected_user_id)
VALUES
(1,2),
(2,1),
(2,3),
(3,1),
(1,3);

INSERT INTO transactions (amount, description, transaction_date, sender, receiver)
VALUES
(8.00, 'Billets de cinéma', '2018-11-12 13:02:56.123456', 2, 1),
(18.00, 'Cadeau d''anniversaire', '2018-12-12 14:02:56.123456', 2, 3),
(20.00, 'Billets de train', '2019-01-12 13:02:56.123456', 1, 2),
(15.00, 'Cadeau à Jimmie', '2019-02-12 14:02:56.123456', 1, 2),
(5.00, 'Mc donalds', '2019-03-12 13:02:56.123456', 3, 1),
(11.00, 'Remboursement', '2019-04-12 14:02:56.123456', 1, 3);