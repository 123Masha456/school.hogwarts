package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByNameAndColor(String name, String color);

    List<Faculty> findAllByColor(String color);

    List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    Optional<Faculty> findById(Long id);

    Optional<Faculty> findByStudents_Id(Long studentId);

}
