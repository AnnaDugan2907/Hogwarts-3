-- Создание таблицы Car
CREATE TABLE Car (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    cost DECIMAL(10, 2) NOT NULL
);

-- Создание таблицы Person
CREATE TABLE Person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    driving_license BOOLEAN NOT NULL DEFAULT FALSE,
    car_id INTEGER REFERENCES Car(id)
);

-- Добавляем ограничение на возраст
ALTER TABLE Person
    ADD CONSTRAINT age_positive CHECK (age > 0);