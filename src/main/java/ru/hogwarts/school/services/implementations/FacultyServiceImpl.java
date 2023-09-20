package ru.hogwarts.school.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.FacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.service.FacultyService;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyServiceImpl implements FacultyService {
    private final Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository,
                              StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }


    @Override
    public Faculty create(Faculty faculty) {
        logger.info("Method CREATE was called with data:" + faculty);

        if (facultyRepository.findByNameAndColor(faculty.getName(), faculty.getColor()).isPresent()) {
            throw new FacultyException("FACULTY IS ALREADY IN TABLE");
        }
        Faculty savedFaculty = facultyRepository.save(faculty);

        logger.info("Returned from method CREATE:" + savedFaculty);

        return savedFaculty;
    }

    @Override
    public Faculty read(long id) {
        logger.info("Method READ was called with data of faculty:" + id);

        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isEmpty()) {
            throw new FacultyException("FACULTY NOT FOUND");
        }
        logger.info("Returned from method READ:" + faculty);

        return faculty.get();
    }


    @Override
    public Faculty update(Faculty faculty) {
        logger.info("Method UPDATE was called with data:" + faculty);

        if (facultyRepository.findById(faculty.getId()).isEmpty()) {
            throw new FacultyException("FACULTY NOT FOUND");
        }

        Faculty updatedFaculty = facultyRepository.save(faculty);

        logger.info("Returned from method UPDATE;" + updatedFaculty);

        return updatedFaculty;
    }

    @Override
    public Faculty delete(long id) {
        logger.info("Method DELETE was called with data of faculty:" + id);

        Optional<Faculty> faculty = facultyRepository.findById(id);
        if (faculty.isEmpty()) {
            throw new FacultyException("FACULTY NOT FOUND");
        }
        facultyRepository.deleteById(id);

        logger.info("Returned from method DELETE:" + faculty);

        return faculty.get();
    }

    @Override
    public List<Faculty> readAll(String color) {
        logger.info("Method READ ALL was called with data:" + color);

        List<Faculty> readAllFaculties = facultyRepository.findAllByColor(color);

        logger.info("Returned from method READ ALL:" + readAllFaculties);

        return readAllFaculties;
    }

    @Override
    public List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color) {
        logger.info("Method FIND BY NAME AND COLOR IGNORE CASE was called with data:"
                + name + color);

        List<Faculty> resultListByNameOrColor =
                facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);

        logger.info("Returned from method FIND BY NAME AND COLOR IGNORE CASE:"
                + resultListByNameOrColor);

        return resultListByNameOrColor;
    }

    public List<Student> findById(long id) {
        logger.info("Method FIND BY ID was called with data of faculty:" + id);

        List<Student> studentsOnFaculty = studentRepository.findByFaculty_id(id);

        logger.info("Returned from method FIND BY ID:" + studentsOnFaculty);

        return studentsOnFaculty;
    }
}

