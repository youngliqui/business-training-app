package businesstrainingapp.services;

import businesstrainingapp.models.Image;
import businesstrainingapp.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image getImage(String filename);

    Image save(MultipartFile file) throws Exception;
    Image save(MultipartFile file, User user) throws Exception;
}
