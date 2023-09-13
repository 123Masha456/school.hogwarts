package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.service.StudentService;

import java.util.List;

@RestController
@RequestMapping(path = "/student")
public class StudentController {
    public final StudentService studentService;


    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @GetMapping(path = "/{id}")
    public Student read(@PathVariable long id) {
        return studentService.read(id);
    }

    @PutMapping
    public Student update(@RequestBody Student student) {
        return studentService.update(student);
    }

    @DeleteMapping(path = "/{id}")
    public Student delete(@PathVariable long id) {
        return studentService.delete(id);
    }

    @GetMapping(path = "/age/{age}")
    public List<Student> readAll(@PathVariable int age) {
        return studentService.readAll(age);
    }

    @GetMapping(path = "/age/min-max")
    public List<Student> getStudentsWhoseAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.getStudentsWhoseAgeBetween(minAge, maxAge);
    }

    @GetMapping(path = "{id}/faculty")
    public Faculty getFacultyOfStudent(@PathVariable long id) {
        studentService.read(id);
        return studentService.getFaculty(id);

    }
}


