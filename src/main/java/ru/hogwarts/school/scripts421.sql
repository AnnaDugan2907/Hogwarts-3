-- Ограничения для таблицы Student
ALTER TABLE Student
    ADD CONSTRAINT age_check CHECK (age >= 16), --Возраст студента не может быть меньше 16 лет.
    ADD CONSTRAINT name_unique UNIQUE (name), --Имена студентов должны быть уникальными
ALTER COLUMN name SET NOT NULL, -- и не равны нулю.
    ALTER COLUMN age SET DEFAULT 20; -- Возраст автоматически присваиваться 20 лет.

-- Ограничение для таблицы Faculty
ALTER TABLE Faculty
    ADD CONSTRAINT name_color_unique UNIQUE (name, color); --Пара “значение названия” - “цвет факультета” должна быть уникальной.