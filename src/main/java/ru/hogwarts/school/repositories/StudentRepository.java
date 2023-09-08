package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByNameAndAge(String name, int age);

    List<Student> findAllByAge(int age);

}
