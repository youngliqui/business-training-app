package businesstrainingapp.servicesImpl;

import businesstrainingapp.DTO.ApplicationTrainingDTO;
import businesstrainingapp.DTO.TrainingRequestInfoDTO;
import businesstrainingapp.exceptions.TrainingRequestNotFoundException;
import businesstrainingapp.exceptions.UserNotFoundException;
import businesstrainingapp.mappers.TrainingMapper;
import businesstrainingapp.models.Training;
import businesstrainingapp.models.TrainingRequest;
import businesstrainingapp.models.User;
import businesstrainingapp.repositories.TrainingRepository;
import businesstrainingapp.repositories.TrainingRequestRepository;
import businesstrainingapp.repositories.UserRepository;
import businesstrainingapp.services.TrainingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Component
@Transactional(readOnly = true)
public class TrainingRequestServiceImpl implements TrainingRequestService {

    private final TrainingRequestRepository trainingRequestRepository;
    private final TrainingRepository trainingRepository;
    private final UserRepository userRepository;

    @Autowired
    public TrainingRequestServiceImpl(TrainingRequestRepository trainingRequestRepository,
                                      TrainingRepository trainingRepository, UserRepository userRepository) {
        this.trainingRequestRepository = trainingRequestRepository;
        this.trainingRepository = trainingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<TrainingRequestInfoDTO> getAllTrainingRequests() {
        return TrainingMapper.TRAINING_MAPPER.toListTrainingRequestsInfoDTO(trainingRequestRepository.findAll());
    }

    @Override
    @Transactional
    public TrainingRequest createTrainingRequest(ApplicationTrainingDTO trainingDTO, String trainerName) {
        User trainer = userRepository.findByName(trainerName).orElseThrow(
                () -> new UserNotFoundException("user with name - " + trainerName + " was not found")
        );
        trainingDTO.setTrainer(trainer);

        return trainingRequestRepository.save(TrainingMapper.TRAINING_MAPPER.toTrainingRequest(trainingDTO));
    }

    @Override
    @Transactional
    public void approveTrainingRequestById(Long trainingId) {
        TrainingRequest trainingRequest = trainingRequestRepository.findById(trainingId).orElseThrow(
                () -> new TrainingRequestNotFoundException("training request with id - " + trainingId + " was not found")
        );

        Training training = TrainingMapper.TRAINING_MAPPER.toTraining(trainingRequest);
        training.setAvailable(true);

        trainingRepository.save(training);
        trainingRequestRepository.delete(trainingRequest);
    }

    @Override
    @Transactional
    public void rejectTrainingRequestById(Long trainingId) {
        TrainingRequest trainingRequest = trainingRequestRepository.findById(trainingId).orElseThrow(
                () -> new TrainingRequestNotFoundException("training request with id - " + trainingId + " was not found")
        );

        trainingRequestRepository.delete(trainingRequest);
    }
}
