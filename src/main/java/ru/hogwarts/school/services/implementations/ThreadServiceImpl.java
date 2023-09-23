package ru.hogwarts.school.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.services.service.StudentService;
import ru.hogwarts.school.services.service.ThreadService;

import java.util.List;

@Service
public class ThreadServiceImpl implements ThreadService {
    private Logger logger = LoggerFactory.getLogger(ThreadServiceImpl.class);

    private StudentService studentService;

    public ThreadServiceImpl(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void thread() {
        List<Student> all = studentService.findAll();
        logStudent(all.get(0));
        logStudent(all.get(1));

        new Thread(() -> {
            logStudent(all.get(2));
            logStudent(all.get(3));
        }).start();

        new Thread(() -> {
            logStudent(all.get(4));
            logStudent(all.get(5));
        }).start();

    }

    private void logStudent(Student student) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
        logger.info(student.toString());
    }

    @Override
    public  void thread1() {
        List<Student> all = studentService.findAll();
        logStudent1(all.get(0));
        logStudent1(all.get(1));

        new Thread(() -> {
            logStudent1(all.get(2));
            logStudent1(all.get(3));
        }).start();

        new Thread(() -> {
            logStudent1(all.get(4));
            logStudent1(all.get(5));
        }).start();
    }

    private synchronized void logStudent1(Student student) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
        logger.info(student.toString());
    }
}
