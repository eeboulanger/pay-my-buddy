
CREATE TABLE IF NOT EXISTS client (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(100) NOT NULL,
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  UNIQUE (email));

CREATE TABLE IF NOT EXISTS account (
  id INT AUTO_INCREMENT PRIMARY KEY,
  balance DECIMAL(8,2) NOT NULL DEFAULT 0.00,
  client_id INT NOT NULL,
  FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS connection (
  connection_id INT NOT NULL,
  client_id INT NOT NULL,
  connection_name VARCHAR(50) NOT NULL,
  PRIMARY KEY (connection_id, client_id),
  FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS transaction (
  id INT AUTO_INCREMENT PRIMARY KEY,
  connection_id INT NOT NULL,
  client_id INT NOT NULL,
  amount DECIMAL(8,2) NOT NULL,
  date DATETIME NOT NULL,
  description VARCHAR(100),
  FOREIGN KEY (connection_id, client_id) REFERENCES connection(connection_id, client_id) ON DELETE NO ACTION ON UPDATE NO ACTION);

