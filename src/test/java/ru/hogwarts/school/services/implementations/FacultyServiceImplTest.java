package ru.hogwarts.school.services.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exceptions.FacultyException;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FacultyServiceImplTest {
    FacultyServiceImpl underTest = new FacultyServiceImpl();
    Faculty faculty = new Faculty(0L, "Kogtevran", "blue");


    @Test
    void createFaculty_facultyIsNotInMap_facultyCreatedAndReturned() {
        Faculty result = underTest.create(faculty);
        assertEquals(faculty, result);
        assertEquals(1, result.getId());
    }

    @Test
    void createFaculty_facultyIsAlreadyInMap_thrownException() {
        underTest.create(faculty);
        FacultyException ex = assertThrows(FacultyException.class,
                () -> underTest.create(faculty));
        assertEquals("FACULTY IS ALREADY IN MAP", ex.getMessage());
    }

    @Test
    void readFaculty_facultyIsInMap_returnFaculty() {
        underTest.create(faculty);
        Faculty result = underTest.read(1);
        assertEquals(faculty, result);
        assertEquals(1, result.getId());
    }

    @Test
    void readFaculty_facultyIsNotInMap_returnFaculty() {
        FacultyException ex = assertThrows(FacultyException.class,
                () -> underTest.read(1));
        assertEquals("FACULTY NOT FOUND", ex.getMessage());
    }

    @Test
    void updateFaculty_facultyIsInMap_facultyUpdatedAndReturned() {
        underTest.create(faculty);
        Faculty result = underTest.update(faculty);
        assertEquals(faculty, result);
        assertEquals(1, result.getId());
    }

    @Test
    void updateFaculty_facultyIsNotInMap_facultyUpdatedAndReturned() {
        FacultyException ex = assertThrows(FacultyException.class,
                () -> underTest.update(faculty));
        assertEquals("FACULTY NOT FOUND", ex.getMessage());
    }

    @Test
    void deleteFaculty_facultyIsInMap_facultyDeletedAndReturned() {
        underTest.create(faculty);
        Faculty result = underTest.delete(1);
        assertEquals(faculty, result);
        assertEquals(1, result.getId());
    }

    @Test
    void deleteFaculty_facultyIsNotInMap_facultyDeletedAndReturned() {
        FacultyException ex = assertThrows(FacultyException.class,
                () -> underTest.delete(1));
        assertEquals("FACULTY NOT FOUND", ex.getMessage());
    }

    @Test
    void readAllFacultiesWhoseColorsAreEqual__returnListOfFaculties() {
        Faculty faculty1 = new Faculty(0L, "Slizerin", "yellow");
        Faculty faculty2 = new Faculty(0L, "Griffindor", "rose");
        Faculty faculty3 = new Faculty(0L, "Puffenduy", "blue");
        underTest.create(faculty);
        underTest.create(faculty1);
        underTest.create(faculty2);
        underTest.create(faculty3);
        List<Faculty> result = underTest.readAll("blue");
        assertIterableEquals(List.of(faculty, faculty3), result);
    }


}
