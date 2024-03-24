DROP DATABASE IF EXISTS jiring_db;
DROP user IF EXISTS 'news'@localhost;
create database IF NOT EXISTS cjiring_db;
create user IF NOT EXISTS news@localhost identified by '123654';
grant all privileges on jiring_db.* To news@localhost;