CREATE TABLE IF NOT EXISTS gift_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS giftItems (
    id INT AUTO_INCREMENT PRIMARY KEY,
    what TEXT NOT NULL,
    whenis DATE NOT NULL,
    who TEXT NOT NULL,
    why TEXT NOT NULL,
    howMuch TEXT NOT NULL,
    needReturn VARCHAR(5) NOT NULL,
    hasGaveReturn VARCHAR(5) NOT NULL
);

INSERT INTO gift_users (name, password) 
VALUES ('yamada','$2a$12$N/k4HeW.r1u3NLg9Q8piYOecakaLtmAdOVOIm7xJlHdcZ.tA2qMMW')
ON DUPLICATE KEY UPDATE name=VALUES(name);