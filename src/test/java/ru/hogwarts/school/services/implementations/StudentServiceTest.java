package ru.hogwarts.school.services.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exceptions.StudentException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    StudentRepository testRepository;
    FacultyRepository facultyRepositoryTest;

    @InjectMocks
    StudentServiceImpl underTest;

    Student student = new Student(0L, "Harry", 15);


    @BeforeEach
    void beforeEach() {
        underTest = new StudentServiceImpl(testRepository, facultyRepositoryTest);
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

    @Test
    void getStudentsWhoseAgeBetweenMinAgeAndMaxAge__returnListOfStudents() {
        Student student1 = new Student(0L, "Ron", 12);
        Student student2 = new Student(0L, "Hermiona", 10);
        Student student3 = new Student(0L, "Gregory", 15);
        when(testRepository.findByAgeBetween(8, 13)).thenReturn(List.of(student1, student2));
        var result = underTest.getStudentsWhoseAgeBetween(8, 13);
        assertEquals(List.of(student1, student2), result);
    }

    @Test
    void getFacultyOfStudent_studentIsInTable_returnFacultyOfStudent() {
        Faculty faculty2 = new Faculty(0L, "Griffindor", "rose");
        student.setFaculty(faculty2);
        when(testRepository.findById(student.getId())).thenReturn(Optional.of(student));
        var result = underTest.read(student.getId()).getFaculty();
        assertEquals(faculty2, result);
    }

    @Test
    void findTotalNumberOfStudents__returnTotalNumber() {
        Student student1 = new Student(2L, "Germiona", 16);

        testRepository.save(student);
        testRepository.save(student1);

        when(testRepository.findTotalNumberOfStudents()).thenReturn(2);
        var result = underTest.findTotalNumberOfStudents();
        assertEquals(2, result);
    }

    @Test
    void findAvgAgeOfStudents__returnAvgAge() {
        Student student1 = new Student(2L, "Germiona", 16);
        Student student2 = new Student(3L, "Ron", 12);

        testRepository.save(student);
        testRepository.save(student1);
        testRepository.save(student2);

        when(testRepository.findAvgAgeOfStudents())
                .thenReturn((student.getAge() + student1.getAge() + student2.getAge()) / 3);

        var result = underTest.findAvgAgeOfStudents();
        assertEquals(43 / 3, result);
    }

    @Test
    void findLastFiveStudents__returnListOfStudents() {
        Student student1 = new Student(2L, "Germiona", 16);
        Student student2 = new Student(3L, "Ron", 12);
        Student student3 = new Student(4L, "Drago", 17);
        Student student4 = new Student(5L, "Gregory", 8);
        Student student5 = new Student(6L, "Fiona", 9);

        testRepository.save(student);
        testRepository.save(student1);
        testRepository.save(student2);
        testRepository.save(student3);
        testRepository.save(student4);
        testRepository.save(student5);

        when(testRepository.findLastStudents(5))
                .thenReturn(List.of(student1, student2, student3, student4, student5));

        var result = underTest.findLastFiveStudents();

        assertEquals(List.of(student1, student2, student3, student4, student5), result);
    }

}

