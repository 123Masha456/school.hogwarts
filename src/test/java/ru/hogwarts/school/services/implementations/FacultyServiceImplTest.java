package ru.hogwarts.school.services.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exceptions.FacultyException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.services.service.FacultyService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceImplTest {
    @Mock
    FacultyRepository testRepository;
    @InjectMocks
    FacultyServiceImpl underTest;
    Faculty faculty = new Faculty(0L, "Kogtevran", "blue");

    @BeforeEach
    void beforeEach() {
        underTest = new FacultyServiceImpl(testRepository);
    }


    @Test
    void createFaculty_facultyIsNotInMap_facultyCreatedAndReturned() {
        when(testRepository.findByNameAndColor(faculty.getName(), faculty.getColor()))
                .thenReturn(Optional.empty());
        when(testRepository.save(faculty)).thenReturn(faculty);
        var actual = underTest.create(faculty);
        var result = faculty;
        assertEquals(actual, result);
    }

    @Test
    void createFaculty_facultyIsAlreadyInTable_thrownException() {
        when(testRepository.findByNameAndColor(faculty.getName(), faculty.getColor()))
                .thenReturn(Optional.of(faculty));
        FacultyException ex = assertThrows(FacultyException.class,
                () -> underTest.create(faculty));
        assertEquals("FACULTY IS ALREADY IN TABLE", ex.getMessage());
    }

    @Test
    void readFaculty_facultyIsInTable_returnFaculty() {
        when(testRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));
        var actual = underTest.read(faculty.getId());
        var result = faculty;
        assertEquals(actual, result);
    }

    @Test
    void readFaculty_facultyIsNotInTable_thrownException() {
        when(testRepository.findById(faculty.getId()))
                .thenReturn(Optional.empty());
        FacultyException ex = assertThrows(FacultyException.class,
                () -> underTest.read(faculty.getId()));
        assertEquals("FACULTY NOT FOUND", ex.getMessage());
    }

    @Test
    void updateFaculty_facultyIsInTable_facultyUpdatedAndReturned() {
        when(testRepository.findById(faculty.getId())).thenReturn(Optional.ofNullable(faculty));
        var actual = underTest.update(faculty);
        var result = testRepository.save(faculty);
        assertEquals(actual, result);
    }

    @Test
    void updateFaculty_facultyIsNotInTable_thrownException() {
        when(testRepository.findById(faculty.getId()))
                .thenReturn(Optional.empty());
        FacultyException ex = assertThrows(FacultyException.class,
                () -> underTest.update(faculty));
        assertEquals("FACULTY NOT FOUND", ex.getMessage());
    }

    @Test
    void deleteFaculty_facultyIsInTable_facultyDeletedAndReturned() {
        when(testRepository.findById(faculty.getId())).thenReturn(Optional.of(faculty));
        var actual = underTest.delete(faculty.getId());
        var result = faculty;
        assertEquals(actual, result);
    }

    @Test
    void deleteFaculty_facultyIsNotInTable_thrownException() {
        when(testRepository.findById(faculty.getId()))
                .thenReturn(Optional.empty());
        FacultyException ex = assertThrows(FacultyException.class,
                () -> underTest.delete(faculty.getId()));
        assertEquals("FACULTY NOT FOUND", ex.getMessage());
    }

    @Test
    void readAllFacultiesWhoseColorsAreEqual__returnListOfFaculties() {
        Faculty faculty1 = new Faculty(0L, "Slizerin", "yellow");
        Faculty faculty2 = new Faculty(0L, "Griffindor", "rose");
        Faculty faculty3 = new Faculty(0L, "Puffenduy", "blue");
        when(testRepository.findAllByColor("blue")).thenReturn(List.of(faculty, faculty3));
        var result = underTest.readAll("blue");
        assertIterableEquals(List.of(faculty, faculty3), result);
    }
}

