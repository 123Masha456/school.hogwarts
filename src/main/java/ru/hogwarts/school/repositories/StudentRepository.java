package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByNameAndAge(String name, int age);

    List<Student> findAllByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findByFaculty_id(long FacultyId);

    @Query("select count(student) from Student student ")
    Integer findTotalNumberOfStudents();

    @Query("select avg(student.age) from Student student")
    Integer findAvgAgeOfStudents();

    @Query(value = "select * from student order by id desc limit :size", nativeQuery = true)
    List<Student> findLastStudents(int size);


}
