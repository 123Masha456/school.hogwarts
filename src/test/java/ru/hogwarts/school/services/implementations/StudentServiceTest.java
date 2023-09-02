package ru.hogwarts.school.services.implementations;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exceptions.StudentException;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    StudentServiceImpl underTest = new StudentServiceImpl();
    Student student = new Student(0L, "Harry", 15);

    @Test
    void createStudent_studentIsNotInMap_studentCreatedAndReturned() {

        Student result = underTest.create(student);
        assertEquals(student, result);
        assertEquals(1, student.getId());
    }

    @Test
    void createStudent_studentIsAlreadyInMap_thrownException() {
        underTest.create(student);
        StudentException ex = assertThrows(StudentException.class,
                () -> underTest.create(student));
        assertEquals("STUDENT IS ALREADY IN MAP", ex.getMessage());
    }

    @Test
    void readStudent_studentIsInMap_returnStudent() {
        underTest.create(student);
        Student result = underTest.read(1);
        assertEquals(student, result);
        assertEquals(1, student.getId());
    }

    @Test
    void readStudent_studentIsNotInMap_returnStudent() {
        StudentException ex = assertThrows(StudentException.class,
                () -> underTest.read(1));
        assertEquals("STUDENT NOT FOUND", ex.getMessage());
    }

    @Test
    void updateStudent_studentIsInMap_studentUpdatedAndReturned() {
        underTest.create(student);
        Student result = underTest.update(student);
        assertEquals(student, result);
        assertEquals(1, result.getId());
    }

    @Test
    void updateStudent_studentIsNotInMap_studentUpdatedAndReturned() {
        StudentException ex = assertThrows(StudentException.class,
                () -> underTest.update(student));
        assertEquals("STUDENT NOT FOUND", ex.getMessage());
    }

    @Test
    void deleteStudent_studentIsInMap_studentDeletedAndReturned() {
        underTest.create(student);
        Student result = underTest.delete(1);
        assertEquals(student, result);
        assertEquals(1, result.getId());
    }

    @Test
    void deleteStudent_studentIsNotMap_studentDeletedAndReturned() {
        StudentException ex = assertThrows(StudentException.class,
                () -> underTest.delete(1));
        assertEquals("STUDENT NOT FOUND", ex.getMessage());
    }

    @Test
    void getAllStudentsWhoseAgesAreEqual__returnListOfStudents() {
        Student student1 = new Student(0L, "Ron", 12);
        Student student2 = new Student(0L, "Hermiona", 10);
        Student student3 = new Student(0L, "Gregory", 15);
        underTest.create(student);
        underTest.create(student1);
        underTest.create(student2);
        underTest.create(student3);
        List<Student> result = underTest.readAll(15);
        assertIterableEquals(List.of(student, student3), result);
    }
}
