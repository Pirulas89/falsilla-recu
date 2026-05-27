CREATE DATABASE IF NOT EXISTS repasoDB;
USE repasoDB;

DROP TABLE IF EXISTS cancion;
DROP TABLE IF EXISTS edificio;
DROP TABLE IF EXISTS ordenador;

CREATE TABLE cancion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    duracion INT NOT NULL
);

CREATE TABLE edificio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    num_viviendas INT NOT NULL,
    anio_edificacion INT NOT NULL,
    rehabilitado BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE ordenador (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tam_ram INT NOT NULL,
    tam_disco INT NOT NULL,
    num_usb INT NOT NULL,
    precio DOUBLE NOT NULL,
    unidades INT NOT NULL
);

INSERT INTO cancion (titulo, duracion) VALUES ('Bohemian Rhapsody', 354);
INSERT INTO cancion (titulo, duracion) VALUES ('Blinding Lights', 200);
INSERT INTO cancion (titulo, duracion) VALUES ('Short Song', 120);

INSERT INTO edificio (num_viviendas, anio_edificacion, rehabilitado) VALUES (12, 1975, false);
INSERT INTO edificio (num_viviendas, anio_edificacion, rehabilitado) VALUES (8, 2001, true);

INSERT INTO ordenador (tam_ram, tam_disco, num_usb, precio, unidades) VALUES (8, 512, 3, 599.99, 10);
INSERT INTO ordenador (tam_ram, tam_disco, num_usb, precio, unidades) VALUES (16, 1024, 4, 899.99, 5);
