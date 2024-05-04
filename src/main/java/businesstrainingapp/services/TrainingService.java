package businesstrainingapp.services;

import businesstrainingapp.DTO.SignUpForTraining;
import businesstrainingapp.DTO.TrainingInfoDTO;
import businesstrainingapp.DTO.UserInfoDTO;
import businesstrainingapp.models.Training;

import java.util.List;

public interface TrainingService {
    List<TrainingInfoDTO> getAllAvailable();

    List<UserInfoDTO> getAllUsersByTrainingId(Long trainingId);

    void updateTrainingAvailability();

    void registrationForTraining(Long trainingId, String username, SignUpForTraining signUpForTraining);

    Training getTrainingById(Long trainingId);

    boolean checkTrainer(String trainerName, Training training);

    boolean checkUser(String username, Long trainingId);

    void cancelTrainingAppointment(Long trainingId, String username);

    List<TrainingInfoDTO> getFilteredAndSortedInfo(String trainerName, String sortBy, String branch, int page, int size);
}
