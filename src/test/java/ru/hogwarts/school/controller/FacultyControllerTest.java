package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.services.implementations.FacultyServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FacultyController.class})
public class FacultyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    FacultyController facultyController;

    @SpyBean
    FacultyServiceImpl facultyService;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    FacultyRepository facultyRepository;

    @MockBean
    StudentRepository studentRepository;

    Faculty faculty = new Faculty(1L, "Kogtevran", "yellow");

    @Test
    void create__returnStatus200AndSavedToDb() throws Exception {
        when(facultyRepository.save(faculty)).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void read__returnStatus200AndFaculty() throws Exception {
        when(facultyRepository.findById(faculty.getId()))
                .thenReturn(Optional.of(faculty));

        mockMvc.perform(get("/faculty/" + faculty.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));
    }

    @Test
    void update__returnStatus200AndUpdatedFaculty() throws Exception {
        when(facultyRepository.findById(faculty.getId()))
                .thenReturn(Optional.of(faculty));
        when(facultyRepository.save(faculty))
                .thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .content(objectMapper.writeValueAsString(faculty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(faculty.getName()))
                .andExpect(jsonPath("$.color").value(faculty.getColor()));

    }

    @Test
    void delete__returnStatus400() throws Exception {
        when(facultyRepository.findById(faculty.getId()))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/faculty/" + faculty.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void readAll__status200AndListOfStudents() throws Exception {
        Faculty faculty1 = new Faculty(2L, "Griffindor", "yellow");
        when(facultyRepository.findAllByColor("yellow"))
                .thenReturn(List.of(faculty, faculty1));

        mockMvc.perform(get("/faculty/color/" + "yellow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").
                        value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").
                        value(faculty.getColor()))
                .andExpect(jsonPath("$[1].name").
                        value(faculty1.getName()))
                .andExpect(jsonPath("$[1].color").
                        value(faculty1.getColor()));

    }

    @Test
    void findByNameOrColorIgnoreCase__status200AndReturnedListOfFaculty() throws Exception {
        Faculty faculty1 = new Faculty(2L, "Griffindor", "red");
        Faculty faculty2 = new Faculty(3L, "Slizerin", "yellow");

        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase("Kogtevran", "yellow"))
                .thenReturn(List.of(faculty, faculty2));

        mockMvc.perform(get("/faculty/name-color?name=Kogtevran&color=yellow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$[0].name").
                        value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").
                        value(faculty.getColor()))

                .andExpect(jsonPath("$[1].name").
                        value(faculty2.getName()))
                .andExpect(jsonPath("$[1].color").
                        value(faculty2.getColor()));
    }

}
