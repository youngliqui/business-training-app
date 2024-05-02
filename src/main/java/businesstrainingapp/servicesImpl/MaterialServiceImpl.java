package businesstrainingapp.servicesImpl;

import businesstrainingapp.exceptions.TrainingNotFoundException;
import businesstrainingapp.models.MaterialEntity;
import businesstrainingapp.models.Training;
import businesstrainingapp.repositories.MaterialRepository;
import businesstrainingapp.repositories.TrainingRepository;
import businesstrainingapp.services.MaterialService;
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
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final TrainingRepository trainingRepository;


    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository, TrainingRepository trainingRepository) {
        this.materialRepository = materialRepository;
        this.trainingRepository = trainingRepository;
    }

    @Override
    @Transactional
    public Long saveFile(MultipartFile file, Long trainingId) throws IOException {
        Training training = trainingRepository.findById(trainingId).orElseThrow(
                () -> new TrainingNotFoundException("training with id - " + trainingId + " was not found")
        );

        MaterialEntity material = MaterialEntity.builder()
                .data(file.getBytes())
                .extensionFile(file.getContentType())
                .filename(file.getOriginalFilename())
                .training(training)
                .build();

        return materialRepository.save(material).getId();
    }

    @Override
    public MaterialEntity getFileByFileId(Long fileId) throws FileNotFoundException {
        return materialRepository.findById(fileId).orElseThrow(
                () -> new FileNotFoundException("file with id - " + fileId + " was not found")
        );
    }

    @Override
    public List<MaterialEntity> getFilesByTrainingId(Long trainingId) {
        return materialRepository.findAllByTrainingId(trainingId);
    }
}
