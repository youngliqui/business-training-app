package businesstrainingapp.servicesImpl;

import businesstrainingapp.DTO.SignUpForTraining;
import businesstrainingapp.DTO.TrainingInfoDTO;
import businesstrainingapp.DTO.UserInfoDTO;
import businesstrainingapp.exceptions.TrainingFullException;
import businesstrainingapp.exceptions.TrainingNotFoundException;
import businesstrainingapp.exceptions.UserNotFoundException;
import businesstrainingapp.mappers.TrainingMapper;
import businesstrainingapp.mappers.UserMapper;
import businesstrainingapp.models.Training;
import businesstrainingapp.models.User;
import businesstrainingapp.repositories.TrainingRepository;
import businesstrainingapp.repositories.UserRepository;
import businesstrainingapp.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Component
@Transactional(readOnly = true)
public class TrainingServiceImpl implements TrainingService {
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingServiceImpl(UserRepository userRepository, TrainingRepository trainingRepository) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    public List<TrainingInfoDTO> getAllAvailable() {
        return TrainingMapper.TRAINING_MAPPER.toListTrainingInfoDTO(
                trainingRepository.findAllByAvailableIsTrueOrderByDateTimeAsc()
        );
    }

    @Override
    public List<UserInfoDTO> getAllUsersByTrainingId(Long trainingId) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new TrainingNotFoundException("training with id - " + trainingId + " was not found")
        );

        return UserMapper.USER_MAPPER.toListUserInfoDTO(training.getUsers());
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    @Async
    public void updateTrainingAvailability() {
        List<Training> trainings = trainingRepository.findAllByAvailableTrue();
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (Training training : trainings) {
            if (training.getDateTime().isBefore(currentDateTime)) {
                training.setAvailable(false);
                trainingRepository.save(training);
            }
        }
    }

    @Override
    @Transactional
    public void registrationForTraining(Long trainingId, String username, SignUpForTraining signUpForTraining) {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with name - " + username + " was not found")
        );

        if (!Objects.equals(user.getName(), signUpForTraining.getName()) ||
                !Objects.equals(user.getEmail(), signUpForTraining.getEmail())) {
            throw new IllegalArgumentException("name or email does not match yours");
        }

        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new TrainingNotFoundException("training with id - " + trainingId + " was not found")
        );

        if (training.getUsersAmount() == training.getTotalSeats()) {
            throw new TrainingFullException("Access to training denied. Group is full.");
        }

        if (!training.isAvailable()) {
            throw new IllegalArgumentException("this training is not available");
        }

        if (user.getUserTrainings().contains(training)) {
            throw new IllegalArgumentException("you have already signed up for this training");
        }

        training.addUser(user);
        user.addUserTraining(training);

        trainingRepository.save(training);
        userRepository.save(user);
    }

    @Override
    public Training getTrainingById(Long trainingId) {
        return trainingRepository.findById(trainingId).orElseThrow(
                () -> new TrainingNotFoundException("training with id - " + trainingId + " was not found")
        );
    }

    @Override
    public boolean checkTrainer(String trainerName, Training training) {
        User trainer = userRepository.findByName(trainerName).orElseThrow(
                () -> new UserNotFoundException("trainer with name - " + trainerName + " was not found")
        );

        User trainingTrainer = training.getTrainer();

        return trainer.getId().equals(trainingTrainer.getId()) && trainer.getName().equals(trainingTrainer.getName());
    }

    @Override
    public boolean checkUser(String username, Long trainingId) {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with name - " + username + " was not found")
        );

        return trainingRepository.existsByIdAndUsersId(trainingId, user.getId());
    }

    @Override
    @Transactional
    public void cancelTrainingAppointment(Long trainingId, String username) {
        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new TrainingNotFoundException("training with id - " + trainingId + " was not found")
        );
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with name - " + username + " was not found")
        );

        training.getUsers().remove(user);
        user.getUserTrainings().remove(training);

        trainingRepository.save(training);
        userRepository.save(user);
    }
}
