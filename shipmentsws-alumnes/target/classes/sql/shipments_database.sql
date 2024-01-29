CREATE DATABASE shipmentsdb CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE shipmentsdb;

-- MySQL
CREATE USER 'dbsuser'@'localhost' IDENTIFIED WITH mysql_native_password BY 'marianao';	

-- MariaDB
SET old_passwords=0;
CREATE USER 'dbsuser'@'localhost' IDENTIFIED BY 'marianao';


GRANT USAGE ON *.* TO 'dbsuser'@'localhost' REQUIRE NONE WITH 
MAX_QUERIES_PER_HOUR 0 
MAX_CONNECTIONS_PER_HOUR 0 
MAX_UPDATES_PER_HOUR 0 
MAX_USER_CONNECTIONS 0;
GRANT ALL PRIVILEGES ON shipmentsdb.* TO 'dbsuser'@'localhost'; 
