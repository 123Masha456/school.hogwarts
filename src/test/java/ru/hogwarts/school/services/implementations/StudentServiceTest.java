package ru.hogwarts.school.services.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exceptions.StudentException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    StudentRepository testRepository;

    @InjectMocks
    StudentServiceImpl underTest;

    Student student = new Student(0L, "Harry", 15);


    @BeforeEach
    void beforeEach() {
        underTest = new StudentServiceImpl(testRepository);
    }

    @Test
    void createStudent_studentIsNotInTable_studentCreatedAndReturned() {
        when(testRepository.findByNameAndAge(student.getName(), student.getAge()))
                .thenReturn(Optional.empty());
        when(testRepository.save(student)).thenReturn(student);
        var actual = underTest.create(student);
        var result = student;
        assertEquals(actual, result);
    }

    @Test
    void createStudent_studentIsAlreadyInTable_thrownException() {
        when(testRepository.findByNameAndAge(student.getName(), student.getAge()))
                .thenReturn(Optional.of(student));
        StudentException ex = assertThrows(StudentException.class,
                () -> underTest.create(student));
        assertEquals("STUDENT IS ALREADY IN TABLE", ex.getMessage());
    }

    @Test
    void readStudent_studentIsInTable_returnStudent() {
        when(testRepository.findById(student.getId())).thenReturn(Optional.of(student));
        var actual = underTest.read(student.getId());
        var result = student;
        assertEquals(result, actual);
    }

    @Test
    void readStudent_studentIsNotInTable_thrownException() {
        when(testRepository.findById(student.getId()))
                .thenReturn(Optional.empty());
        StudentException ex = assertThrows(StudentException.class,
                () -> underTest.read(student.getId()));
        assertEquals("STUDENT NOT FOUND", ex.getMessage());
    }

    @Test
    void updateStudent_studentIsInTable_studentUpdatedAndReturned() {
        when(testRepository.findById(student.getId())).thenReturn(Optional.ofNullable(student));
        var actual = underTest.update(student);
        var result = testRepository.save(student);
        assertEquals(result, actual);
    }

    @Test
    void updateStudent_studentIsNotInTable_thrownException() {
        when(testRepository.findById(student.getId()))
                .thenReturn(Optional.empty());
        StudentException ex = assertThrows(StudentException.class,
                () -> underTest.update(student));
        assertEquals("STUDENT NOT FOUND", ex.getMessage());
    }

    @Test
    void deleteStudent_studentIsInTable_studentDeletedAndReturned() {
        when(testRepository.findById(student.getId())).thenReturn(Optional.of(student));
        var actual = underTest.delete(student.getId());
        var result = student;
        assertEquals(actual, result);
    }

    @Test
    void deleteStudent_studentIsNotTable_thrownException() {
        when(testRepository.findById(student.getId()))
                .thenReturn(Optional.empty());
        StudentException ex = assertThrows(StudentException.class,
                () -> underTest.delete(student.getId()));
        assertEquals("STUDENT NOT FOUND", ex.getMessage());
    }

    @Test
    void getAllStudentsWhoseAgesAreEqual__returnListOfStudents() {
        Student student1 = new Student(0L, "Ron", 12);
        Student student2 = new Student(0L, "Hermiona", 10);
        Student student3 = new Student(0L, "Gregory", 15);
        when(testRepository.findAllByAge(15)).thenReturn(List.of(student, student3));
        var result = underTest.readAll(15);
        assertEquals(List.of(student, student3), result);
    }
}
