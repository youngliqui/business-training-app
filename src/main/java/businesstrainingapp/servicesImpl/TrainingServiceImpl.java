package businesstrainingapp.servicesImpl;

import businesstrainingapp.DTO.SignUpForTraining;
import businesstrainingapp.DTO.TrainingInfoDTO;
import businesstrainingapp.DTO.UserInfoDTO;
import businesstrainingapp.exceptions.TrainingFullException;
import businesstrainingapp.exceptions.TrainingNotFoundException;
import businesstrainingapp.exceptions.UserNotFoundException;
import businesstrainingapp.mappers.UserMapper;
import businesstrainingapp.models.Training;
import businesstrainingapp.models.User;
import businesstrainingapp.repositories.TrainingRepository;
import businesstrainingapp.repositories.UserRepository;
import businesstrainingapp.services.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static businesstrainingapp.mappers.TrainingMapper.TRAINING_MAPPER;

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
        return TRAINING_MAPPER.toListTrainingInfoDTO(
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

    @Override
    public List<TrainingInfoDTO> getFilteredAndSortedInfo(String trainerName, String sortBy, String branch,
                                                          int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Training> trainingPage = null;

        if (trainerName == null && branch == null) {
            if (sortBy != null && sortBy.equals("dateAsc")) {
                trainingPage = trainingRepository.findAllByOrderByDateTimeAsc(pageRequest);
            } else if (sortBy != null && sortBy.equals("dateDesc")) {
                trainingPage = trainingRepository.findAllByOrderByDateTimeDesc(pageRequest);
            } else {
                trainingPage = trainingRepository.findAll(pageRequest);
            }
        }

        if (trainerName != null && branch == null) {
            if (sortBy != null && sortBy.equals("dateAsc")) {
                trainingPage = trainingRepository.findAllByTrainerNameContainingIgnoreCaseOrderByDateTimeAsc(
                        trainerName, pageRequest);
            } else if (sortBy != null && sortBy.equals("dateDesc")) {
                trainingPage = trainingRepository.findAllByTrainerNameContainingIgnoreCaseOrderByDateTimeDesc(
                        trainerName, pageRequest);
            } else {
                trainingPage = trainingRepository.findAllByTrainerNameContainingIgnoreCase(trainerName, pageRequest);
            }
        }

        if (trainerName == null && branch != null) {
            if (sortBy != null && sortBy.equals("dateAsc")) {
                trainingPage = trainingRepository.findAllByBranchContainingIgnoreCaseOrderByDateTimeAsc(
                        branch, pageRequest);
            } else if (sortBy != null && sortBy.equals("dateDesc")) {
                trainingPage = trainingRepository.findAllByBranchContainingIgnoreCaseOrderByDateTimeDesc(
                        branch, pageRequest);
            } else {
                trainingPage = trainingRepository.findAllByBranchContainingIgnoreCase(branch, pageRequest);
            }
        }

        if (trainerName != null && branch != null) {
            if (sortBy != null && sortBy.equals("dateAsc")) {
                trainingPage = trainingRepository
                        .findAllByTrainerNameContainingIgnoreCaseAndBranchContainingIgnoreCaseOrderByDateTimeAsc(
                                trainerName, branch, pageRequest);
            } else if (sortBy != null && sortBy.equals("dateDesc")) {
                trainingPage = trainingRepository
                        .findAllByTrainerNameContainingIgnoreCaseAndBranchContainingIgnoreCaseOrderByDateTimeDesc(
                                trainerName, branch, pageRequest);
            } else {
                trainingPage = trainingRepository
                        .findAllByTrainerNameContainingIgnoreCaseAndBranchContainingIgnoreCase(
                                trainerName, branch, pageRequest);
            }
        }

        return TRAINING_MAPPER.toListTrainingInfoDTO(trainingPage.getContent());
    }
}
