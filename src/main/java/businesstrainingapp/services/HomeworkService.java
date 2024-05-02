package businesstrainingapp.services;

import businesstrainingapp.models.HomeworkEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface HomeworkService {
    Long saveFile(MultipartFile file, String username, Long trainingId) throws IOException;

    HomeworkEntity getFileByFileId(Long fileId) throws FileNotFoundException;

    List<HomeworkEntity> getFilesByUserIdAndTrainingId(Long userId, Long trainingId);

    List<HomeworkEntity> getFilesByTrainingId(Long trainingId);
}
