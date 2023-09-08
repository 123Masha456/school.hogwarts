package ru.hogwarts.school.services.implementations;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exceptions.StudentException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student create(Student student) {
        if (studentRepository.findByNameAndAge(student.getName(), student.getAge()).isPresent()) {
            throw new StudentException("STUDENT IS ALREADY IN TABLE");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student read(long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            throw new StudentException("STUDENT NOT FOUND");
        }
        return student.get();
    }

    @Override
    public Student update(Student student) {
        if (studentRepository.findById(student.getId()).isEmpty()) {
            throw new StudentException("STUDENT NOT FOUND");
        }
        return studentRepository.save(student);
    }

    @Override
    public Student delete(long id) {
        Optional<Student> student = studentRepository.findById(id);
        if (student.isEmpty()) {
            throw new StudentException("STUDENT NOT FOUND");
        }
        studentRepository.deleteById(id);
        return student.get();
    }

    @Override
    public List<Student> readAll(int age) {
        return studentRepository.findAllByAge(age);
    }
}
