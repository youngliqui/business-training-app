package businesstrainingapp.controllers;

import businesstrainingapp.DTO.ImageSaveResult;
import businesstrainingapp.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/images")
@Tag(name = "Изображения", description = "методы для работы с изображениями")
@SecurityRequirement(name = "basicAuth")
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/db/{filename}")
    @Operation(summary = "Извлечь изображение по названию")
    public ResponseEntity<Resource> retrieve(@PathVariable String filename) {
        var image = imageService.getImage(filename);
        var body = new ByteArrayResource(image.getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, image.getMimeType())
                .body(body);
    }

    @PostMapping("/db/upload")
    @Operation(summary = "Загрузить изображение в БД")
    public ImageSaveResult upload(@RequestPart MultipartFile file) {
        try {
            var image = imageService.save(file);
            return ImageSaveResult.builder()
                    .error(false)
                    .filename(image.getFilename())
                    .link(createImageLink(image.getFilename()))
                    .build();
        } catch (Exception e) {
            log.error("Failed to save image", e);
            return ImageSaveResult.builder().error(true).filename(file.getOriginalFilename()).build();
        }
    }

    @PostMapping("/db/upload_multi")
    @Operation(summary = "Загрузка нескольких изображений в БД")
    public List<ImageSaveResult> uploadMulti(@RequestPart List<MultipartFile> files) {
        return files.stream()
                .map(this::upload)
                .collect(Collectors.toList());
    }

    private String createImageLink(String filename) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/images/db/" + filename)
                .toUriString();
    }
}
