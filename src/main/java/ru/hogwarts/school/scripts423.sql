-- Первый JOIN-запрос: информаця обо всех студентах школы Хогвартс вместе с названиями факультетов.
SELECT s.name AS student_name, s.age, f.name AS faculty_name
FROM Student s
         JOIN Faculty f ON s.faculty_id = f.id;

-- Второй JOIN-запрос: студенты, у которых есть аватарки.
SELECT s.name AS student_name, s.age
FROM Student s
         JOIN Avatar a ON s.id = a.student_id;