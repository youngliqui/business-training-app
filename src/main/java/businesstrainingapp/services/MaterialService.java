package businesstrainingapp.services;

import businesstrainingapp.models.MaterialEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface MaterialService {
    Long saveFile(MultipartFile file, Long trainingId) throws IOException;

    MaterialEntity getFileByFileId(Long fileId) throws FileNotFoundException;

    List<MaterialEntity> getFilesByTrainingId(Long trainingId);
}
