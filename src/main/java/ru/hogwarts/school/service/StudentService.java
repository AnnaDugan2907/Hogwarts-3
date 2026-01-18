package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents(PageRequest.of(0, 5));
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student");
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        logger.debug("Deleting student with id: {}", id);
        try {
            studentRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting student with id = {}", id, e);
            throw e;
        }
    }

    public Collection<Student> getAllStudent() {
        logger.info("Was invoked method for get all student");
        return studentRepository.findAll();
    }


    public Student findByName(String name) {
        logger.info("Was invoked method for find by name");
        return studentRepository.findByNameIgnoreCase(name);
    }

    public Collection<Student> findStudentByName(String name) {
        logger.info("Was invoked method for find by name");

        return studentRepository.findStudentByNameContainsIgnoreCase(name);
    }

    public Collection<Student> findAllByNameContains(String namePart) {
        logger.info("Was invoked method for find by name");

        return studentRepository.findAllByNameContainsIgnoreCase(namePart);
    }

    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for find by name");

        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Long getStudentsCount() {
        logger.info("Was invoked method for find by name");

        return studentRepository.countAllStudents();
    }

    public double avgAgeByStudents() {
        logger.info("Was invoked method for find by name");

        return studentRepository.getAverageAge();
    }

    public List<Student> getLastFiveStudent() {
        logger.info("Was invoked method for find by name");

        return studentRepository.findLastFiveStudents(PageRequest.of(0, 5));
    }

    public List<String> getNamesStartingWithA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.toUpperCase().startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAgeAllStudents() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    public void printParallel() {
        System.out.println(studentRepository.findStudentById(1L));
        System.out.println(studentRepository.findStudentById(2L));
        new Thread(() -> {
            System.out.println(studentRepository.findStudentById(3L));
            System.out.println(studentRepository.findStudentById(4L));
        }).start();
        new Thread(() -> {
            System.out.println(studentRepository.findStudentById(5L));
            System.out.println(studentRepository.findStudentById(67L));
        }).start();
    }

    public synchronized void printSynchronized() {
        System.out.println(studentRepository.findStudentById(1L));
        System.out.println(studentRepository.findStudentById(2L));
        new Thread(() -> {
            printSync(3L);
            printSync(4L);
        }).start();
        new Thread(() -> {
            printSync(5L);
            printSync(6L);
        }).start();
    }

    public void printSync(Long studentId) {
        synchronized (StudentService.class) {
            System.out.println(studentRepository.findStudentById(studentId));
        }
    }
}