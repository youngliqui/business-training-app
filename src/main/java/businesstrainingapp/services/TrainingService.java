package businesstrainingapp.services;

import businesstrainingapp.DTO.SignUpForTraining;
import businesstrainingapp.DTO.TrainingInfoDTO;
import businesstrainingapp.DTO.UserInfoDTO;

import java.util.List;

public interface TrainingService {
    List<TrainingInfoDTO> getAllAvailable();

    List<UserInfoDTO> getAllUsersByTrainingId(Long trainingId);

    void updateTrainingAvailability();

    void registrationForTraining(Long trainingId, String username, SignUpForTraining signUpForTraining);
}
