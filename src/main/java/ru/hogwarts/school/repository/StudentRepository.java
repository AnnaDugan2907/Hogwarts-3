package ru.hogwarts.school.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

@Repository
public interface  StudentRepository extends JpaRepository<Student, Long> {
    Student findByNameIgnoreCase(String name);

    Collection<Student> findStudentByNameContainsIgnoreCase(String name);

    Collection<Student> findAllByNameContainsIgnoreCase(String namePart);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);


    // Получить количество всех студентов в школе.  Эндпоинт должен вернуть число.
    @Query("SELECT COUNT(s) FROM Student s" )
    Long countAllStudents();

    // Получить средний возраст студентов. Эндпоинт должен вернуть число.
    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAge();

    // Получать только пять последних студентов. Последние студенты считаются теми, у кого идентификатор больше других.
    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findLastFiveStudents(Pageable pageable);

}
