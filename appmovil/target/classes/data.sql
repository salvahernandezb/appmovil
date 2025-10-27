/* Datos de ejemplo para desarrollo/testing */
INSERT INTO usuario (nombre, mail, numero_movil)
VALUES ('Carlos', 'carlos@mail.com', '04141234567'),
       ('Maria', 'maria@mail.com', '04241234567');

INSERT INTO cupo (numero_movil, saldo, datos, plataforma, usuario_id)
VALUES 
('04141234567', 30.5, 2.5, 'prepago', 1),
('04241234567', 50.0, 4.0, 'postpago', 2);