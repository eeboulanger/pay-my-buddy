
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(100) NOT NULL,
  UNIQUE (email));

CREATE TABLE IF NOT EXISTS account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  balance DOUBLE NOT NULL DEFAULT 0.00,
  user_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS user_connection (
  user_id INT NOT NULL,
  connected_user_id INT NOT NULL,
  PRIMARY KEY (user_id, connected_user_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (connected_user_id) REFERENCES users(id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS transaction (
  id INT AUTO_INCREMENT PRIMARY KEY,
  amount DOUBLE NOT NULL,
  description VARCHAR(100) NOT NULL,
  date DATETIME NOT NULL,
  sender INT NOT NULL,
  receiver INT NOT NULL,
  FOREIGN KEY (sender) REFERENCES users(id),
  FOREIGN KEY (receiver) REFERENCES users(id));

