package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        return optionalFaculty.orElse(null); // Возвращает null, если не найден
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findByNameIgnoreCase(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Faculty> findByColorIgnoreCase(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }
}
