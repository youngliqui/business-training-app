package businesstrainingapp.controllers;

import businesstrainingapp.DTO.SignUpForTraining;
import businesstrainingapp.DTO.TrainingInfoDTO;
import businesstrainingapp.DTO.UserInfoDTO;
import businesstrainingapp.exceptions.UserNotAuthorizeException;
import businesstrainingapp.services.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainings")
@Tag(name = "Тренинги", description = "методы для работы с тренингами")
@SecurityRequirement(name = "basicAuth")
public class TrainingController {
    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    @Operation(summary = "получение доступных тренингов")
    public List<TrainingInfoDTO> getAllAvailable() {
        trainingService.updateTrainingAvailability();

        return trainingService.getAllAvailable();
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
    @Operation(summary = "просмотра списка записавшихся пользователей")
    public List<UserInfoDTO> getUsersByTraining(@PathVariable("id") Long trainingId) {
        return trainingService.getAllUsersByTrainingId(trainingId);
    }
}
