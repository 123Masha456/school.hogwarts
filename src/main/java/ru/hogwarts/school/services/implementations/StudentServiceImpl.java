package ru.hogwarts.school.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.StudentException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentServiceImpl(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Student create(Student student) {
        logger.info("Method CREATE was called with data:" + student);

        if (studentRepository.findByNameAndAge(student.getName(), student.getAge()).isPresent()) {
            throw new StudentException("STUDENT IS ALREADY IN TABLE");
        }

        Student savedStudent = studentRepository.save(student);

        logger.info("Returned from method CREATE:" + savedStudent);

        return savedStudent;
    }

    @Override
    public Student read(long id) {
        logger.info("Method READ was called with data of student:" + id);

        Optional<Student> student = studentRepository.findById(id);

        if (student.isEmpty()) {
            throw new StudentException("STUDENT NOT FOUND");
        }

        logger.info("Returned from method READ:" + student);

        return student.get();

    }

    @Override
    public Student update(Student student) {
        logger.info("Method UPDATE was called with data:" + student);

        if (studentRepository.findById(student.getId()).isEmpty()) {
            throw new StudentException("STUDENT NOT FOUND");
        }
        Student updatedStudent = studentRepository.save(student);

        logger.info("Returned from method UPDATE:" + updatedStudent);

        return updatedStudent;
    }

    @Override
    public Student delete(long id) {
        logger.info("Method DELETE was called with data of student:" + id);

        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            throw new StudentException("STUDENT NOT FOUND");
        }
        studentRepository.deleteById(id);

        logger.info("Returned from method DELETE:" + student);

        return student.get();
    }

    @Override
    public List<Student> readAll(int age) {
        logger.info("Method READ ALL was called with data:" + age);

        List<Student> readAllStudent = studentRepository.findAllByAge(age);

        logger.info("Returned from method READ ALL:" + readAllStudent);

        return readAllStudent;
    }

    @Override
    public List<Student> getStudentsWhoseAgeBetween(int minAge, int maxAge) {
        logger.info("Method GET STUDENTS WHOSE AGE BETWEEN was called with data:"
                + minAge + maxAge);

        List<Student> resultListByAgeBetween = studentRepository.findByAgeBetween(minAge, maxAge);

        logger.info("Returned from method GET STUDENTS WHOSE AGE BETWEEN:" + resultListByAgeBetween);

        return resultListByAgeBetween;
    }

    public Faculty getFaculty(long id) {
        logger.info("Method GET FACULTY was called with data of student's id");
        Student student = read(id);
        logger.info("Returned faculty of student:" + student.getFaculty());
        return student.getFaculty();
    }

    @Override
    public Integer findTotalNumberOfStudents() {
        logger.info("Method FIND TOTAL NUMBER OF STUDENTS was called");
        Integer total = studentRepository.findTotalNumberOfStudents();
        logger.info("Returned total number of students with data:" + total);
        return total;
    }

    @Override
    public Integer findAvgAgeOfStudents() {
        logger.info("Method FIND AVERAGE AGE OF STUDENTS was called");
        Integer avgAge = studentRepository.findAvgAgeOfStudents();
        logger.info("Returned average age of students with data:" + avgAge);
        return avgAge;
    }

    @Override
    public List<Student> findLastFiveStudents() {
        logger.info("Method FIND LAST FIVE STUDENTS was called");
        List<Student> lastFiveStudents = studentRepository.findLastStudents(5);
        logger.info("Returned List of last five students with data:" + lastFiveStudents);
        return lastFiveStudents;
    }
}


