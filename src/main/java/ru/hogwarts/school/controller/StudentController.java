package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private StudentRepository studentRepository;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}") // GET
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping //GET
    public ResponseEntity findStudent(@RequestParam(required = false) String name) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(studentService.findByName(name));
        }
        return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping("/getStudentByAge") //GET
    public ResponseEntity getStudentByAgeRange(@RequestParam int minAge,
                                               @RequestParam int maxAge) {
        return ResponseEntity.ok(studentService.findByAgeBetween(minAge, maxAge));
    }

    @GetMapping("{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null || student.getFaculty() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student.getFaculty());
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/names-starting-with-a")
    public List<String> nameWithA() {
        return studentService.getNamesStartingWithA();
    }

    @GetMapping("/avg-age-all-students")
    public Double averageAgeAllStudents() {
        return studentService.getAverageAgeAllStudents();
    }

    @PostMapping //POST
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping //PUT
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student existingStudent = studentService.findStudent(student.getId());
        if (existingStudent == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // возвращаем 404, если студента нет
        }
        Student updatedStudent = studentService.editStudent(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("{id}") //DELETE
    public ResponseEntity deleteStudent(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
}
