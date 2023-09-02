package ru.hogwarts.school.services.implementations;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.services.service.FacultyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();
    private long counter;


    @Override
    public Faculty create(Faculty faculty) {
        if (facultyMap.containsValue(faculty)) {
            throw new FacultyException("FACULTY IS ALREADY IN MAP");
        }
        faculty.setId(++counter);
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty read(long id) {
        if (!facultyMap.containsKey(id)) {
            throw new FacultyException("FACULTY NOT FOUND");
        }
        return facultyMap.get(id);
    }


    @Override
    public Faculty update(Faculty faculty) {
        if (!facultyMap.containsKey(faculty.getId())) {
            throw new FacultyException("FACULTY NOT FOUND");
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty delete(long id) {
        Faculty faculty = facultyMap.remove(id);
        if (faculty == null) {
            throw new FacultyException("FACULTY NOT FOUND");
        }
        facultyMap.remove(id);
        return faculty;
    }

    @Override
    public List<Faculty> readAll(String color) {
        return facultyMap.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toUnmodifiableList());
    }
}
