package ru.hogwarts.school.controller;

import org.apache.coyote.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @AfterEach
    void afterEach() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
        ;
    }

    Student student = new Student(1L, "Ron", 10);
    String url = "http://localhost:";

    @Test
    void create__returnStatus200AndStudent() {
        ResponseEntity<Student> studentResponseEntity =
                restTemplate.postForEntity(url + port + "/student",
                        student, Student.class);
        assertEquals(HttpStatus.OK, studentResponseEntity.getStatusCode());
        assertEquals(student.getName(), studentResponseEntity.getBody().getName());
        assertEquals(student.getAge(), studentResponseEntity.getBody().getAge());
    }

    @Test
    void read__returnBadRequest() {
        ResponseEntity<String> stringResponseEntity =
                restTemplate.getForEntity(url + port + "/student/" + student.getId(),
                        String.class);
        assertEquals(HttpStatus.BAD_REQUEST, stringResponseEntity.getStatusCode());
        assertEquals("STUDENT NOT FOUND", stringResponseEntity.getBody());
    }

    @Test
    void update__returnStatus200AndChangeStudentsParameter() {
        studentRepository.save(student);
        Student resultStudent = studentRepository.save(student);
        resultStudent.setName("Harry");
        studentRepository.save(resultStudent);

        ResponseEntity<Student> response = restTemplate.exchange(
                url + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<Student>(resultStudent),
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultStudent, response.getBody());
        assertEquals("Harry", Objects.requireNonNull(response.getBody()).getName());

    }

    @Test
    void delete__status200() {
        studentRepository.save(student);
        Student resultStudent = studentRepository.save(student);

        ResponseEntity<Student> response = restTemplate.exchange(
                url + port + "/student/" + resultStudent.getId(),
                HttpMethod.DELETE,
                null,
                Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultStudent, response.getBody());
    }

    @Test
    void readAll__returnStatus200AndListOfStudents() {

        Student student1 = new Student(2L, "Gregory", 10);
        studentRepository.save(student);
        studentRepository.save(student1);

        ResponseEntity<List<Student>> exchange = restTemplate.exchange(
                url + port + "/student/age/" + student.getAge(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertEquals(List.of(student, student1), exchange.getBody());

    }

    @Test
    void getStudentsWhoseAgeBetween__returnStatus200AndListOfStudents() {
        var miAge = 9;
        var maxAge = 16;

        Student student1 = new Student(2L, "Gregory", 15);
        Student student2 = new Student(3L, "Ron", 18);
        studentRepository.save(student);
        studentRepository.save(student1);
        studentRepository.save(student2);

        ResponseEntity<List<Student>> exchange = restTemplate.exchange(
                url + port + "/student/age/" + (miAge) + "/" + (maxAge),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        assertEquals(HttpStatus.OK, exchange.getStatusCode());
        assertEquals(List.of(student, student1), exchange.getBody());
    }

    @Test
    void getFacultyOfStudent__returnStatus200AndFaculty() {
        Faculty faculty = new Faculty(1L, "Griffindor", "black");
        facultyRepository.save(faculty);
        Faculty resultFaculty = facultyRepository.save(faculty);
        studentRepository.save(student);
        student.setFaculty(resultFaculty);
        studentRepository.save(student);
        Student resultStudent = studentRepository.save(student);


        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                url + port + "/student/" + resultStudent.getId() + "/faculty", Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(resultStudent.getFaculty(), response.getBody());

    }

}