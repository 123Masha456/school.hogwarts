package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.services.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping(path = "/avatar")
public class AvatarController {
    private final AvatarService avatarService;
    private final int maxFileSizeInKb = 300;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }
    @PostMapping(value = "/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar)
    throws IOException {
        if(avatar.getSize() > 1024 *maxFileSizeInKb){
            return ResponseEntity.badRequest().body("IMAGE IS TOO LARGE");
        }
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok().body("IMAGE SUCCESSFULLY SAVED");
    }
    @GetMapping(value = "/{id}/avatar-from-Db")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable long id){
        Avatar avatar = avatarService.readFromDb(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.ok().headers(headers).body(avatar.getData());
    }
    @GetMapping(value = "/{id}/avatar-from-file")
    public void downloadAvatar(@PathVariable long id, HttpServletResponse response) throws IOException{
        Avatar avatar = avatarService.readFromDb(id);
        Path path = Path.of(avatar.getFilePath());
        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();
                ){
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }
}
