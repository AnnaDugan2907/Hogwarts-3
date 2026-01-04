package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private FacultyRepository facultyRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculties";
    }

    @Test
    public void testGetFacultyByIdSuccess() {
        Faculty faculty = new Faculty();
        faculty.setName("Test Faculty");
        faculty.setColor("Blue");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Faculty createdFaculty = createResponse.getBody();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/" + createdFaculty.getId(), Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Faculty", response.getBody().getName());
    }

    @Test
    public void testEditFacultySuccess() {
        Faculty faculty = new Faculty();
        faculty.setName("Old Name");
            faculty.setColor("White");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Faculty createdFaculty = createResponse.getBody();

        createdFaculty.setName("Updated Name");
        HttpEntity<Faculty> request = new HttpEntity<>(createdFaculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(getBaseUrl(), HttpMethod.PUT, request, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    public void testEditFacultyBadRequest() {
        Faculty faculty = new Faculty();
        faculty.setId(9999L); // Несуществующий ID
        HttpEntity<Faculty> request = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                request,
                Faculty.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // Ожидаем 404
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("To Delete");
        faculty.setColor("Yellow");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(
                getBaseUrl(),
                faculty,
                Faculty.class
        );
        Faculty createdFaculty = createResponse.getBody();
        restTemplate.delete(getBaseUrl() + "/" + createdFaculty.getId());

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdFaculty.getId(),
                Faculty.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteFacultyNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/9999",
                HttpMethod.DELETE,
                null,
                Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSearchFacultiesByName() {
        Faculty faculty = new Faculty();
        faculty.setName("Search Test");
        faculty.setColor("Purple");
        restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(getBaseUrl() + "/searchNameOrColor?name=Search Test", Collection.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetFacultyStudentsSuccess() {

        Faculty faculty = new Faculty();
        faculty.setName("Faculty with Students");
        faculty.setColor("Green");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity(getBaseUrl(), faculty, Faculty.class);
        Faculty createdFaculty = facultyResponse.getBody();

        ResponseEntity<Collection> response = restTemplate.getForEntity(getBaseUrl() + "/" + createdFaculty.getId() + "/students", Collection.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetFaculty() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculties", String.class))
                .isNotEmpty();
    }

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(facultyController).isNotNull();
    }

    @Test
    public void testPostBooks() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Bloody");
        faculty.setColor("red");
        Assertions.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculties", faculty, String.class))
                .isNotEmpty();
    }
}
