package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    private final AvatarRepository avatarRepository;

    @Value("${avatar.cover.dir.path}") // Добавленная аннотация
    private String avatarDir; // Объявление переменной


    public AvatarController(AvatarService avatarService, AvatarRepository avatarRepository) {
        this.avatarService = avatarService;
        this.avatarRepository = avatarRepository;
    }

    @PostMapping(value = "/{studentId}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // Путь для добавления обложки к студенту
    public ResponseEntity<String> addCover(@PathVariable Long studentId, @RequestParam MultipartFile file) {
        try {
            avatarService.addCover(studentId, file); // Вызов метода добавления обложки
            return ResponseEntity.ok("Обложка добавлена.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Ошибка при добавлении обложки: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{studentId}/cover")
    public ResponseEntity<byte[]> getCover(@PathVariable Long studentId) {
        try {
            Avatar avatar = avatarService.findAvatar(studentId);    // Находим объект Avatar по studentId
            Path filePath = Path.of(avatarDir, studentId + "." + getExtension(avatar.getFilePath()));

            byte[] image = Files.readAllBytes(filePath);   // Читаем содержимое файла в байтовый массив

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
            headers.setContentLength(image.length);

            return ResponseEntity.ok().headers(headers).body(image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Ошибка при загрузке обложки: " + e.getMessage()).getBytes());
        }
    }

    private String getExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }


    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping
    public Page<Avatar> getAvatars(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }
}
