package businesstrainingapp.controllers;

import businesstrainingapp.DTO.*;
import businesstrainingapp.exceptions.UserNotAuthorizeException;
import businesstrainingapp.mappers.TrainingMapper;
import businesstrainingapp.models.Training;
import businesstrainingapp.services.HomeworkService;
import businesstrainingapp.services.MaterialService;
import businesstrainingapp.services.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/trainings")
@Tag(name = "Тренинги", description = "методы для работы с тренингами")
@SecurityRequirement(name = "basicAuth")
public class TrainingController {
    private final TrainingService trainingService;
    private final MaterialService materialService;
    private final HomeworkService homeworkService;

    @Autowired
    public TrainingController(TrainingService trainingService, MaterialService materialService, HomeworkService homeworkService) {
        this.trainingService = trainingService;
        this.materialService = materialService;
        this.homeworkService = homeworkService;
    }

    @GetMapping
    @Operation(summary = "Получение всех доступных тренингов")
    public List<TrainingInfoDTO> getAllAvailable() {
        trainingService.updateTrainingAvailability();

        return trainingService.getAllAvailable();
    }

    @GetMapping(params = {"page", "size"})
    @Operation(summary = "получение страниц с тренингами")
    public List<TrainingInfoDTO> getFilteredAndSortedTrainings(
            @RequestParam(value = "trainer", required = false) String trainerName,
            @Parameter(description = "сортировка по {dateAsc/dateDesc}")
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "branch", required = false) String branch,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        return trainingService.getFilteredAndSortedInfo(trainerName, sortBy, branch, page, size);
    }

    @PostMapping("/{id}/register")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "регистрация пользователя на тренинг")
    public ResponseEntity<Void> registrationForTraining(@PathVariable("id") Long trainingId, Principal principal,
                                                        @RequestBody SignUpForTraining signUpForTraining) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        trainingService.registrationForTraining(trainingId, principal.getName(), signUpForTraining);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}/users")
    @PreAuthorize("hasAuthority('TRAINER')")
    @Operation(summary = "просмотр списка записавшихся пользователей")
    public List<UserInfoDTO> getUsersByTraining(@PathVariable("id") Long trainingId) {
        return trainingService.getAllUsersByTrainingId(trainingId);
    }

    @GetMapping("/{id}/trainer-dashboard")
    @PreAuthorize("hasAuthority('TRAINER')")
    @Operation(summary = "получении информации о тренинге для её создателя")
    public TrainingInfoForTrainerDTO getTrainingInfoForTrainerById(@PathVariable("id") Long trainingId,
                                                                   Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        Training training = trainingService.getTrainingById(trainingId);
        if (!trainingService.checkTrainer(principal.getName(), training)) {
            throw new UserNotAuthorizeException("You are not the trainer of this training");
        }

        return TrainingMapper.TRAINING_MAPPER.toTrainingForTrainerDTO(training);
    }

    @GetMapping("/{id}/homeworks")
    @PreAuthorize("hasAuthority('TRAINER')")
    @Operation(summary = "получение домашних заданий тренинга")
    public List<HomeworkInfoDTO> getHomeworksForTraining(@PathVariable("id") Long trainingId, Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        Training training = trainingService.getTrainingById(trainingId);
        if (!trainingService.checkTrainer(principal.getName(), training)) {
            throw new UserNotAuthorizeException("You are not the trainer of this training");
        }

        return TrainingMapper.TRAINING_MAPPER.toListHomeworkInfoDTO(homeworkService.getFilesByTrainingId(trainingId));
    }

    @PostMapping("/{id}/homeworks")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "добавление домашней работы пользователем")
    public ResponseEntity<Void> addHomework(@PathVariable("id") Long trainingId, Principal principal,
                                            @RequestPart MultipartFile file) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        if (!trainingService.checkUser(principal.getName(), trainingId)) {
            throw new UserNotAuthorizeException("You are not the user of this training");
        }

        try {
            homeworkService.saveFile(file, principal.getName(), trainingId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/materials")
    @PreAuthorize("hasAuthority('TRAINER')")
    @Operation(summary = "добавление материалов к тренингу")
    public ResponseEntity<Void> addMaterial(@PathVariable("id") Long trainingId, Principal principal,
                                            @RequestPart MultipartFile file) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }
        Training training = trainingService.getTrainingById(trainingId);
        if (!trainingService.checkTrainer(principal.getName(), training)) {
            throw new UserNotAuthorizeException("You are not the trainer of this training");
        }

        try {
            materialService.saveFile(file, trainingId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("{id}/materials")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Просмотр всех материалов тренинга")
    public List<MaterialInfoDTO> getAllMaterials(@PathVariable("id") Long trainingId, Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        if (!trainingService.checkUser(principal.getName(), trainingId)) {
            throw new UserNotAuthorizeException("You are not the user of this training");
        }

        return TrainingMapper.TRAINING_MAPPER.mapMaterialsToDTO(materialService.getFilesByTrainingId(trainingId));
    }

    @GetMapping("{id}/cancel")
    @Operation(summary = "Отмена записи на тренинг")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> cancelAppointment(@PathVariable("id") Long trainingId, Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }
        if (!trainingService.checkUser(principal.getName(), trainingId)) {
            throw new UserNotAuthorizeException("You are not the user of this training");
        }

        trainingService.cancelTrainingAppointment(trainingId, principal.getName());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
