package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {InfoController.class})
public class InfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    InfoController infoController;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${server.port}")
    private Integer port;


    @Test
    void getPort__returnedPort() throws Exception {
        mockMvc.perform(get("/info/getPort").
                        content(objectMapper.writeValueAsString(port)).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());

    }
}
