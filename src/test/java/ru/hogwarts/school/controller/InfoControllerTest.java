package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {InfoController.class})
public class InfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    InfoController infoController;

    @Test
    void getPort__returnedPort() throws Exception {
        mockMvc.perform(get("/info/getPort"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(8089));
    }
}
