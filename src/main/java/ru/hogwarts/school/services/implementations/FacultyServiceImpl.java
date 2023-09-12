package ru.hogwarts.school.services.implementations;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.service.FacultyService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository,
                              StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public Faculty create(Faculty faculty) {
        if (facultyRepository.findByNameAndColor(faculty.getName(), faculty.getColor()).isPresent()) {
            throw new FacultyException("FACULTY IS ALREADY IN TABLE");
        }
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty read(long id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isEmpty()) {
            throw new FacultyException("FACULTY NOT FOUND");
        }
        return faculty.get();
    }


    @Override
    public Faculty update(Faculty faculty) {
        if (facultyRepository.findById(faculty.getId()).isEmpty()) {
            throw new FacultyException("FACULTY NOT FOUND");
        }
        return facultyRepository.save(faculty);
    }

    @Override
    public Faculty delete(long id) {
        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isEmpty()) {
            throw new FacultyException("FACULTY NOT FOUND");
        }
        facultyRepository.deleteById(id);
        return faculty.get();
    }

    @Override
    public List<Faculty> readAll(String color) {
        return facultyRepository.findAllByColor(color);
    }

    @Override
    public Faculty findByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        Optional<Faculty> faculty = facultyRepository.findByNameAndColor(name, color);
        if (faculty.isEmpty()) {
            throw new FacultyException("FACULTY NOT FOUND");
        } else if (faculty.get().getName() != null && faculty.get().getColor() == null) {
            faculty.get();
        }
        if (faculty.get().getColor() != null && faculty.get().getName() == null) {
            faculty.get();
        }
        return faculty.get();
    }


    public List<Student> findById(long id) {
        return studentRepository.findByFaculty_id(id);
    }
}

