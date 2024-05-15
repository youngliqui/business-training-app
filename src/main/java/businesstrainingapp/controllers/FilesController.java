package businesstrainingapp.controllers;

import businesstrainingapp.services.HomeworkService;
import businesstrainingapp.services.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/files")
@Tag(name = "Изображения", description = "методы для работы с изображениями")
@SecurityRequirement(name = "basicAuth")
public class FilesController {
    private final MaterialService materialService;
    private final HomeworkService homeworkService;

    @Autowired
    public FilesController(MaterialService materialService, HomeworkService homeworkService) {
        this.materialService = materialService;
        this.homeworkService = homeworkService;
    }

    @GetMapping("/materials/{id}")
    @Operation(summary = "Получение материала по id")
    public ResponseEntity<Resource> getMaterialById(@PathVariable("id") Long id) throws FileNotFoundException {
        var material = materialService.getFileByFileId(id);
        var body = new ByteArrayResource(material.getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, material.getExtensionFile())
                .body(body);
    }

    @GetMapping("/homeworks/{id}")
    @Operation(summary = "Получение домашней работы по id")
    public ResponseEntity<Resource> getHomeworkById(@PathVariable("id") Long id) throws FileNotFoundException {
        var homework = homeworkService.getFileByFileId(id);
        var body = new ByteArrayResource(homework.getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, homework.getExtensionFile())
                .body(body);
    }
}
