package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/info")

public class InfoController {
    @Value("${server.port}")
    private Integer port;

    @GetMapping(path = "/getPort")
    public Integer getPort() {
        return port;
    }
}
