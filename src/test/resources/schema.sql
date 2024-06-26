SET REFERENTIAL_INTEGRITY FALSE;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(100),
  role VARCHAR(45) NOT NULL);

DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  balance DOUBLE NOT NULL,
  user_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE);

DROP TABLE IF EXISTS user_connections;

CREATE TABLE user_connections (
  user_id INT NOT NULL,
  connected_user_id INT NOT NULL,
  PRIMARY KEY (user_id, connected_user_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (connected_user_id) REFERENCES users(id) ON DELETE CASCADE);

DROP TABLE IF EXISTS transactions;

CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  amount DOUBLE NOT NULL,
  description VARCHAR(100) NOT NULL,
  transaction_date DATETIME NOT NULL,
  sender INT NOT NULL,
  receiver INT NOT NULL,
  FOREIGN KEY (sender) REFERENCES users(id),
  FOREIGN KEY (receiver) REFERENCES users(id));

SET REFERENTIAL_INTEGRITY TRUE;