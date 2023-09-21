package ru.hogwarts.school.services.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exceptions.AvatarException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.services.service.AvatarService;
import ru.hogwarts.school.services.service.StudentService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarServiceImpl implements AvatarService {
    private final Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    private final String avatarsDir;
    private final StudentService studentService;
    private final AvatarRepository avatarRepository;

    public AvatarServiceImpl(StudentService studentService,
                             AvatarRepository avatarRepository,
                             @Value("${path.to.avatars.folder}") String avatarsDir) {
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
        this.avatarsDir = avatarsDir;
    }

    @Override
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Method UPLOAD AVATAR was called with data:" + studentId + avatarFile);

        Student student = studentService.read(studentId);

        Path filePath = Path.of(avatarsDir, student.getId() + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = avatarRepository.findByStudent_id(studentId).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        avatarRepository.save(avatar);

        logger.info("Method saved:" + avatar);
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private Optional<Avatar> findByStudent_id(long studentId) {
        logger.info("Method FIND BY STUDENT ID was called with data:" + studentId);

        Student student = studentService.read(studentId);
        Optional<Avatar> avatar = avatarRepository.findById(student.getId());
        if (avatarRepository.findByStudent_id(student.getId()).isEmpty()) {
            return Optional.empty();
        }

        logger.info("Returned from method FIND BY STUDENT ID:" + avatar);

        return avatar;
    }

    @Override
    public Avatar readFromDb(long id) {
        logger.info("Method READ FROM DB was called with data of avatar:" + id);

        Avatar result = avatarRepository.findById(id).orElseThrow(() -> new AvatarException("AVATAR NOT FOUND"));

        logger.info("Returned from method READ FROM DB:" + result);

        return result;
    }

    @Override
    public List<Avatar> getPage(int pageNumber, int size) {
        logger.info("Method GET PAGE was called with data:" + pageNumber + size);

        PageRequest request = PageRequest.of(pageNumber, size);

        List<Avatar> avatarListOnPage = avatarRepository.findAll(request).getContent();

        logger.info("Returned from method GET PAGE:" + avatarListOnPage);

        return avatarListOnPage;
    }
}
