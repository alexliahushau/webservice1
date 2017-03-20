CREATE TABLE users (
  login VARCHAR(30),
  firstName VARCHAR (30),
  lastName VARCHAR (30),
  email VARCHAR(50),
  image BLOB,
  PRIMARY KEY (login)
);