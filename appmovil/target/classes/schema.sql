-- Esquema de la base de datos
CREATE DATABASE appmovil_db;
USE appmovil_db;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    mail VARCHAR(100) NOT NULL,
    numero_movil VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE cupos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_movil VARCHAR(20),
    saldo DECIMAL(10,2) DEFAULT 0.00,
    datos DECIMAL(10,2) DEFAULT 0.00,
    plataforma ENUM('prepago', 'postpago') NOT NULL,
    FOREIGN KEY (numero_movil) REFERENCES usuarios(numero_movil)
);