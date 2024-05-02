package businesstrainingapp.servicesImpl;

import businesstrainingapp.exceptions.TrainingNotFoundException;
import businesstrainingapp.exceptions.UserNotFoundException;
import businesstrainingapp.models.HomeworkEntity;
import businesstrainingapp.models.Training;
import businesstrainingapp.models.User;
import businesstrainingapp.repositories.HomeworkRepository;
import businesstrainingapp.repositories.TrainingRepository;
import businesstrainingapp.repositories.UserRepository;
import businesstrainingapp.services.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
@Component
@Transactional(readOnly = true)
public class HomeworkServiceImpl implements HomeworkService {
    private final UserRepository userRepository;
    private final TrainingRepository trainingRepository;
    private final HomeworkRepository homeworkRepository;

    @Autowired
    public HomeworkServiceImpl(UserRepository userRepository, TrainingRepository trainingRepository,
                               HomeworkRepository homeworkRepository) {
        this.userRepository = userRepository;
        this.trainingRepository = trainingRepository;
        this.homeworkRepository = homeworkRepository;
    }

    @Override
    @Transactional
    public Long saveFile(MultipartFile file, String username, Long trainingId) throws IOException {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with name - " + username + " was not found"));

        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new TrainingNotFoundException("training with id - " + trainingId + " was not found")
        );

        HomeworkEntity homework = HomeworkEntity.builder()
                .data(file.getBytes())
                .filename(file.getOriginalFilename())
                .extensionFile(file.getContentType())
                .user(user)
                .training(training)
                .build();

        user.addHomework(homework);
        training.addHomework(homework);

        return homeworkRepository.save(homework).getId();
    }

    @Override
    public HomeworkEntity getFileByFileId(Long fileId) throws FileNotFoundException {
        return homeworkRepository.findById(fileId).orElseThrow(
                () -> new FileNotFoundException("file with id - " + fileId + " was not found")
        );
    }

    @Override
    public List<HomeworkEntity> getFilesByUserIdAndTrainingId(Long userId, Long trainingId) {
        return homeworkRepository.findAllByTrainingIdAndUserId(trainingId, userId);
    }

    @Override
    public List<HomeworkEntity> getFilesByTrainingId(Long trainingId) {
        return homeworkRepository.findAllByTrainingId(trainingId);
    }
}
