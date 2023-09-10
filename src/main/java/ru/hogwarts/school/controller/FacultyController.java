package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.services.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping(path = "/faculty")
public class FacultyController {
    public final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public Faculty create(@RequestBody Faculty faculty) {
        return facultyService.create(faculty);
    }

    @GetMapping(path = "{id}")
    public Faculty read(@PathVariable long id) {
        return facultyService.read(id);
    }

    @PutMapping
    public Faculty update(@RequestBody Faculty faculty) {
        return facultyService.update(faculty);
    }

    @DeleteMapping(path = "{id}")
    public Faculty delete(@PathVariable long id) {
        return facultyService.delete(id);
    }

    @GetMapping(path = "/color/{color}")
    public List<Faculty> readAll(@PathVariable String color) {
        return facultyService.readAll(color);
    }

    @GetMapping(path = "/{name}")
    public List<Faculty> findByNameIgnoreCase(@PathVariable String name){
    return facultyService.findByNameIgnoreCase(name);
    }
    @GetMapping(path = "/{color}")
    public List<Faculty> findByColorIgnoreCase(@PathVariable String color){
        return facultyService.findByColorIgnoreCase(color);
    }
}
