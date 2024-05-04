package businesstrainingapp.controllers;

import businesstrainingapp.DTO.ApplicationTrainingDTO;
import businesstrainingapp.DTO.TrainingRequestInfoDTO;
import businesstrainingapp.exceptions.UserNotAuthorizeException;
import businesstrainingapp.services.TrainingRequestService;
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

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/training-requests")
@Tag(name = "Заявки на тренинг", description = "методы для работы с заявками на тренинг")
@SecurityRequirement(name = "basicAuth")
public class TrainingRequestController {
    private final TrainingRequestService trainingRequestService;

    @Autowired
    public TrainingRequestController(TrainingRequestService trainingRequestService) {
        this.trainingRequestService = trainingRequestService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Получение всех заявок")
    public List<TrainingRequestInfoDTO> getAll() {
        return trainingRequestService.getAllTrainingRequests();
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('TRAINER')")
    @Operation(summary = "Создание заявки на тренинг")
    public ResponseEntity<Void> createTrainingRequest(@RequestBody ApplicationTrainingDTO applicationTrainingDTO,
                                                      Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        trainingRequestService.createTrainingRequest(applicationTrainingDTO, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Принятие заявки на тренинг")
    public ResponseEntity<Void> approveTrainingRequest(@PathVariable("id") Long id) {
        trainingRequestService.approveTrainingRequestById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}/reject")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Отклонение заявки на тренинг")
    public ResponseEntity<Void> rejectTrainingRequest(@PathVariable("id") Long id) {
        trainingRequestService.rejectTrainingRequestById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
