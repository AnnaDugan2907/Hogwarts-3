package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/Student";
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testGetAllStudents() {
        ResponseEntity<Collection> response = restTemplate.getForEntity(baseUrl, Collection.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetStudentByIdSuccess() {
        Student student = new Student();
        student.setName("Test Student");
        student.setAge(20);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(baseUrl, student, Student.class);
        Student createdStudent = createResponse.getBody();

        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + createdStudent.getId(), Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdStudent.getName(), response.getBody().getName());
    }

    @Test
    public void testGetStudentByIdNotFound() {
        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/9999",
                Student.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student();
        student.setName("New Student");
        student.setAge(22);

        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("New Student", response.getBody().getName());
        assertEquals(22, response.getBody().getAge());
    }


    @Test
    public void testGetAllStudentsWhenNameNotProvided() {
        ResponseEntity<Collection> response = restTemplate.getForEntity(baseUrl, Collection.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() >= 0);
    }

    @Test
    public void testGetStudentsByAgeRange() {
        Student student = new Student();
        student.setName("Sam");
        student.setAge(30);
        restTemplate.postForEntity(baseUrl, student, Student.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                baseUrl + "/getStudentByAge?minAge=20&maxAge=40",
                Collection.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    public void testEditStudentSuccess() {
        Student student = new Student();
        student.setName("Old Name");
        student.setAge(18);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(baseUrl, student, Student.class);
        Student createdStudent = createResponse.getBody();

        createdStudent.setName("Updated Name");
        HttpEntity<Student> request = new HttpEntity<>(createdStudent);
        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    public void testEditStudentNotFound() {
        Student student = new Student();
        student.setId(9999L); // Несуществующий ID
        student.setName("Non-existent");
        student.setAge(99);

        HttpEntity<Student> request = new HttpEntity<>(student);
        ResponseEntity<Student> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                Student.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student();
        student.setName("To be deleted");
        student.setAge(25);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(baseUrl, student, Student.class);
        Student createdStudent = createResponse.getBody();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + createdStudent.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<Student> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + createdStudent.getId(),
                Student.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    public void testGetStudentFacultyNotFound() {
        Student student = new Student();
        student.setName("Student without Faculty");
        student.setAge(21);
        ResponseEntity<Student> studentResponse = restTemplate.postForEntity(baseUrl, student, Student.class);
        Student createdStudent = studentResponse.getBody();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/" + createdStudent.getId() + "/faculty",
                Faculty.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
