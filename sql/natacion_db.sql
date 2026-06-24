-- ============================================================
--  Script SQL - Sistema de Registro de Participantes de Natación
--  EPN - ESFOT | Programación Orientada a Objetos
--  Base de datos: PostgreSQL
-- ============================================================

-- INSTRUCCIONES:
-- Conéctate primero como superusuario (postgres) y ejecuta
-- este script completo en pgAdmin o con:
--   psql -U postgres -f natacion_db.sql

-- 1. Crear la base de datos (ejecuta esto separado si usas psql)
CREATE DATABASE natacion_db
    ENCODING 'UTF8'
    LC_COLLATE = 'es_EC.UTF-8'
    LC_CTYPE   = 'es_EC.UTF-8'
    TEMPLATE template0;

-- Luego conéctate a ella: \c natacion_db

-- 2. Crear la tabla de usuarios (para Login)
CREATE TABLE IF NOT EXISTS usuarios (
    id      SERIAL PRIMARY KEY,
    usuario VARCHAR(50)  NOT NULL UNIQUE,
    clave   VARCHAR(100) NOT NULL
);

-- 3. Crear la tabla de participantes
CREATE TABLE IF NOT EXISTS participantes (
    id            SERIAL PRIMARY KEY,
    cedula        VARCHAR(10)  NOT NULL UNIQUE,
    nombre        VARCHAR(80)  NOT NULL,
    apellido      VARCHAR(80)  NOT NULL,
    edad          INT          NOT NULL,
    correo        VARCHAR(120) NOT NULL UNIQUE,
    estado_civil  VARCHAR(20)  NOT NULL CHECK (estado_civil IN ('Soltero','Casado','Divorciado','Viudo')),
    jornada       VARCHAR(15)  NOT NULL CHECK (jornada IN ('Matutina','Vespertina','Nocturna')),
    categoria     VARCHAR(50)  NOT NULL,
    observaciones TEXT
);

-- 4. Insertar usuarios de prueba
INSERT INTO usuarios (usuario, clave) VALUES
    ('admin',    'admin123'),
    ('profesor', 'epn2024');

-- 5. Insertar participantes de prueba
INSERT INTO participantes (cedula, nombre, apellido, edad, correo, estado_civil, jornada, categoria, observaciones) VALUES
    ('1712345678', 'Carlos',   'Pérez',    22, 'carlos.perez@email.com',    'Soltero',    'Matutina',   'Juvenil A', 'Sin observaciones'),
    ('1798765432', 'María',    'Gómez',    17, 'maria.gomez@email.com',     'Soltero',    'Vespertina', 'Juvenil B', 'Alergia al cloro leve'),
    ('1756789012', 'Andrés',   'Torres',   35, 'andres.torres@email.com',   'Casado',     'Nocturna',   'Master',    'Participante frecuente'),
    ('1734561234', 'Lucía',    'Ramírez',  29, 'lucia.ramirez@email.com',   'Divorciado', 'Matutina',   'Adulto',    ''),
    ('1778904321', 'Fernando', 'Castillo', 12, 'fernando.castillo@email.com','Soltero',   'Vespertina', 'Infantil',  'Requiere tutor');
