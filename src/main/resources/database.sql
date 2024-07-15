CREATE DATABASE IF NOT EXISTS `lesson9`;

USE `lesson9`;

CREATE TABLE IF NOT EXISTS `books` (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `title` TEXT NOT NULL,
    `author` TEXT
);

CREATE TABLE IF NOT EXISTS `copies` (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `bookId` INTEGER NOT NULL,
    `condition` INTEGER NOT NULL,

    FOREIGN KEY (`bookId`) REFERENCES `books` (`id`)
);

CREATE TABLE IF NOT EXISTS `customers` (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `name` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `loans` (
    `id` INTEGER PRIMARY KEY AUTO_INCREMENT,
    `copyId` INTEGER NOT NULL,
    `customerId` INTEGER NOT NULL,
    `dateLoaned` DATE NOT NULL,
    `dateDue` DATE NOT NULL,
    `dateReturned` DATE,

    FOREIGN KEY (`copyId`) REFERENCES `copies` (`id`),
    FOREIGN KEY (`customerId`) REFERENCES `customers` (`id`)
);
