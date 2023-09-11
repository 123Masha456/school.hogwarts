package ru.hogwarts.school.services.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FacultyService {
    Faculty create(Faculty faculty);

    Faculty read(long id);

    Faculty update(Faculty faculty);

    Faculty delete(long id);

    List<Faculty> readAll(String color);

    Faculty findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);
}
