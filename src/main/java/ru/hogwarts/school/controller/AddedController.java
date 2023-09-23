package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.services.implementations.ThreadServiceImpl;
import ru.hogwarts.school.services.service.ThreadService;

import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/added")
public class AddedController {
    private ThreadService threadService;

    private AddedController(ThreadService threadService) {
        this.threadService = threadService;
    }

    @GetMapping()
    public Integer sumNumber() {
        long start = System.currentTimeMillis();

        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);

        long finish = System.currentTimeMillis();

        System.out.println(finish - start);

        return sum;
    }

    @GetMapping(path = "/thread")
    public void startTread() {
        threadService.thread();
    }

    @GetMapping(path = "/thread1")
    public void startThread1() {
        threadService.thread1();
    }
}
