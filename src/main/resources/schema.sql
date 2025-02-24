CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  role VARCHAR(20) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
CREATE TABLE IF NOT EXISTS dictionary (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  word VARCHAR(100) NOT NULL UNIQUE,
  language VARCHAR(50) NOT NULL,
  part_of_speech VARCHAR(50) NOT NULL,
  pronunciation VARCHAR(100),
  meaning VARCHAR(1000) NOT NULL,
  example_sentence VARCHAR(1000),
  view_count BIGINT NOT NULL DEFAULT 0,
  user_id BIGINT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS inquiry (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content VARCHAR(2000) NOT NULL,
  author_email VARCHAR(255) NOT NULL,
  resolved BOOLEAN NOT NULL DEFAULT FALSE,
  user_id BIGINT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS persistent_logins (
  username VARCHAR(64) NOT NULL,
  series VARCHAR(64) PRIMARY KEY,
  token VARCHAR(64) NOT NULL,
  last_used TIMESTAMP NOT NULL
);
CREATE INDEX idx_dictionary_word ON dictionary(word);
CREATE INDEX idx_inquiry_author_email ON inquiry(author_email);