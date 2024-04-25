package businesstrainingapp.servicesImpl;

import businesstrainingapp.exceptions.ImageNotFoundException;
import businesstrainingapp.models.Image;
import businesstrainingapp.models.User;
import businesstrainingapp.repositories.ImageRepository;
import businesstrainingapp.services.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Component
@Transactional(readOnly = true)
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image getImage(String filename) {
        return imageRepository.findByFilename(filename).orElseThrow(
                () -> new ImageNotFoundException("image with name - " + filename + " was not found")
        );
    }

    @Override
    @Transactional
    public Image save(MultipartFile file) throws Exception {
        if (imageRepository.existsByFilename(file.getOriginalFilename())) {
            log.info("Image {} have already existed", file.getOriginalFilename());
            return Image.builder().filename(file.getOriginalFilename()).build();
        }

        var image = Image.builder()
                .filename(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .data(file.getBytes())
                .build();

        return imageRepository.save(image);
    }

    @Override
    @Transactional
    public Image save(MultipartFile file, User user) throws Exception {
        if (imageRepository.existsByFilename(file.getOriginalFilename())) {
            log.info("Image {} have already existed", file.getOriginalFilename());
            return Image.builder().filename(file.getOriginalFilename()).build();
        }

        var image = Image.builder()
                .filename(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .data(file.getBytes())
                .user(user)
                .build();

        user.setProfileImage(image);

        return imageRepository.save(image);
    }
}
