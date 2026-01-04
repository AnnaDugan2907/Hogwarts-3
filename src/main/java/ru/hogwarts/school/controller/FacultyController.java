package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculties")
public class FacultyController {

    private final FacultyService facultService;

    public FacultyController(FacultyService facultService) {
        this.facultService = facultService;
    }

    @GetMapping("{id}") // GET
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping //POST
    public Faculty createStudent(@RequestBody Faculty faculty) {
        return facultService.createFaculty(faculty);
    }

    @GetMapping // GET
    public ResponseEntity<Collection<Faculty>> getAllFaculty() {
        return ResponseEntity.ok(facultService.getAllFaculty());
    }

    @GetMapping("/searchNameOrColor")
    public ResponseEntity<Collection<Faculty>> searchFaculties(@RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String color) {
        if (name != null) {
            return ResponseEntity.ok(facultService.findByNameIgnoreCase(name));
        }
        if (color != null) {
            return ResponseEntity.ok(facultService.findByColorIgnoreCase(color));
        }
        return ResponseEntity.ok(null);

    }

    @GetMapping("{id}/students")
    public ResponseEntity<Collection<Student>> getFacultyStudents(@PathVariable Long id) {
        Faculty faculty = facultService.findFaculty(id);
        if (faculty == null || faculty.getStudents() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty.getStudents());
    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty existingFaculty = facultService.findFaculty(faculty.getId());
        if (existingFaculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Faculty updatedFaculty = facultService.editFaculty(faculty);
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        Faculty faculty = facultService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        facultService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}
