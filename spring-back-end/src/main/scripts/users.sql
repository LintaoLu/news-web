CREATE TABLE users (
    user_email VARCHAR(100) NOT NULL,
    user_password VARCHAR(25) NOT NULL,
    user_name VARCHAR(30) NOT NULL,
    active BIT(1),
    roles ENUM('ROLE_USER', 'ROLE_ADMIN') NOT NULL,
    PRIMARY KEY (user_email)
);
