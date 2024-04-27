package businesstrainingapp.services;

import businesstrainingapp.DTO.ApplicationTrainingDTO;
import businesstrainingapp.DTO.TrainingRequestInfoDTO;
import businesstrainingapp.models.TrainingRequest;

import java.util.List;

public interface TrainingRequestService {
    List<TrainingRequestInfoDTO> getAllTrainingRequests();

    TrainingRequest createTrainingRequest(ApplicationTrainingDTO trainingDTO, String trainerName);

    void approveTrainingRequestById(Long trainingId);

    void rejectTrainingRequestById(Long trainingId);
}
