package ru.hogwarts.school.services.implementations;

import nonapi.io.github.classgraph.utils.FileUtils;

import org.junit.jupiter.api.Test;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.AvatarException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.services.service.StudentService;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AvatarServiceImplTest {
    StudentService studentService = mock(StudentService.class);
    AvatarRepository avatarRepository = mock(AvatarRepository.class);
    String avatarsDir = "./src/test/resources/avatar";
    AvatarServiceImpl underTest = new AvatarServiceImpl(studentService, avatarRepository, avatarsDir);
    Student student = new Student(1L, "Ron", 10);

    @Test
    void uploadAvatar__avtarSavedToDbAndDirectory() throws IOException {
        MultipartFile file = new MockMultipartFile(
                "1.pdf", "1.pdf", "pdf", new byte[]{});
        when(studentService.read(student.getId())).thenReturn(student);
        when(avatarRepository.findByStudent_id(student.getId())).thenReturn(Optional.empty());
        underTest.uploadAvatar(1L, file);
        verify(avatarRepository, times(1)).save(any());
        assertTrue(FileUtils.canRead(
                new File(avatarsDir + "/" + student.getId() + "." + file.getContentType())));

    }

    @Test
    void readFromDb_avatarFound_returnAvatar() {
        Avatar avatar = new Avatar();
        when(avatarRepository.findById(student.getId())).thenReturn(Optional.of(avatar));
        var result = underTest.readFromDb(student.getId());
        assertEquals(avatar, result);

    }

    @Test
    void readFromDb_avatarNotFound_thrownException() {
        when(avatarRepository.findById(student.getId())).thenReturn(Optional.empty());
        AvatarException ex = assertThrows(AvatarException.class,
                () -> underTest.readFromDb(student.getId()));
        assertEquals("AVATAR NOT FOUND", ex.getMessage());
    }

    @Test
    void getPage__returnListOfAvatars() {
        Avatar avatar = new Avatar();
        Avatar avatar1 = new Avatar();

        when(avatarRepository.findAll((PageRequest) any()))
                .thenReturn(new PageImpl<>(List.of(avatar, avatar1)));

        List<Avatar> result = underTest.getPage(0, 1);
        assertEquals(List.of(avatar, avatar1), result);
    }
}
