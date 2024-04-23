package businesstrainingapp.servicesImpl;

import businesstrainingapp.DTO.ProfileUserDTO;
import businesstrainingapp.DTO.RegistrationUserDTO;
import businesstrainingapp.DTO.UpdationUserDTO;
import businesstrainingapp.DTO.UserInfoDTO;
import businesstrainingapp.exceptions.PasswordNotMatchException;
import businesstrainingapp.exceptions.UserNotFoundException;
import businesstrainingapp.models.Enums.Role;
import businesstrainingapp.models.Image;
import businesstrainingapp.models.User;
import businesstrainingapp.repositories.ImageRepository;
import businesstrainingapp.repositories.UserRepository;
import businesstrainingapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Component
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ImageRepository imageRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void addUser(RegistrationUserDTO userDTO) {
        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .build();

        if (userDTO.getRole().equals("trainer")) {
            user.setRole(Role.TRAINER);
        } else {
            user.setRole(Role.USER);
        }

        userRepository.save(user);
    }

    @Override
    public List<UserInfoDTO> getAll() {
        return userRepository.findAll().stream()
                .map(user -> UserInfoDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ProfileUserDTO getUserProfileById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id - " + id + " was not found")
        );

        List<String> trainingNames = user.getTrainings().stream()
                .map(training -> training.getTitle() + " - " + training.getDate())
                .toList();

        return ProfileUserDTO.builder()
                .description(user.getDescription())
                .image(user.getProfileImage())
                .rating(user.getRating())
                .trainingArchive(trainingNames)
                .build();
    }

    @Override
    public ProfileUserDTO getUserProfileByName(String name) {
        User user = userRepository.findByName(name).orElseThrow(
                () -> new UserNotFoundException("user with name - " + name + " was not found")
        );

        List<String> trainingNames = user.getTrainings().stream()
                .map(training -> training.getTitle() + " - " + training.getDate())
                .toList();

        return ProfileUserDTO.builder()
                .username(user.getName())
                .description(user.getDescription())
                .image(user.getProfileImage())
                .rating(user.getRating())
                .trainingArchive(trainingNames)
                .build();
    }

    @Override
    @Transactional
    public ProfileUserDTO updateUserProfileByName(String name, UpdationUserDTO userDTO) {
        boolean isChanged = false;

        User user = userRepository.findByName(name).orElseThrow(
                () -> new UserNotFoundException("user with username - " + name + " not found")
        );

        if (!passwordEncoder.matches(userDTO.getCurrentPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("Current password is incorrect");
        }

        if (userDTO.getNewPassword() != null
                && !userDTO.getNewPassword().isEmpty()
                && !Objects.equals(userDTO.getNewPassword(), userDTO.getMatchingNewPassword())) {
            throw new PasswordNotMatchException("Password does not match");
        }

        if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
            isChanged = true;
        }

        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()
                && !Objects.equals(userDTO.getUsername(), user.getName())) {
            user.setName(userDTO.getUsername());
            isChanged = true;
        }

        if (!Objects.equals(userDTO.getEmail(), user.getEmail())) {
            user.setEmail(userDTO.getEmail());
            isChanged = true;
        }
        if (isChanged) {
            userRepository.save(user);
        }

        return ProfileUserDTO.builder()
                .username(user.getName())
                .description(user.getDescription())
                .image(user.getProfileImage())
                .rating(user.getRating())
                .trainingArchive(user.getTrainings().stream()
                        .map(training -> training.getTitle() + " - " + training.getDate())
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id - " + id + " was not found")
        );

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public void deleteUserByName(String name) {
        User user = userRepository.findByName(name).orElseThrow(
                () -> new UserNotFoundException("user with username - " + name + " was not found")
        );

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public ProfileUserDTO addProfileImage(String username, MultipartFile file) throws IOException {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with username - " + username + " was not found")
        );

        Image image = null;

        if (user.getProfileImage() != null) {
            image = user.getProfileImage();
            image.setFilename(file.getOriginalFilename());
            image.setData(file.getBytes());
            image.setMimeType(file.getContentType());
        } else {
            image = Image.builder()
                    .filename(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .data(file.getBytes())
                    .user(user)
                    .build();
            imageRepository.save(image);
        }

        user.setProfileImage(image);
        userRepository.save(user);

        return ProfileUserDTO.builder()
                .description(user.getDescription())
                .image(user.getProfileImage())
                .rating(user.getRating())
                .trainingArchive(user.getTrainings().stream()
                        .map(training -> training.getTitle() + " - " + training.getDate())
                        .toList())
                .build();
    }

    @Override
    @Transactional
    public ProfileUserDTO updateProfileDescription(String username, String description) {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with username - " + username + " was not found")
        );

        user.setDescription(description);
        userRepository.save(user);

        return ProfileUserDTO.builder()
                .description(description)
                .image(user.getProfileImage())
                .rating(user.getRating())
                .trainingArchive(user.getTrainings().stream()
                        .map(training -> training.getTitle() + " - " + training.getDate())
                        .toList())
                .build();
    }

    @Override
    public UserInfoDTO getUserInfoById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id - " + id + " was not found")
        );

        return UserInfoDTO.builder()
                .name(user.getName())
                .role(user.getRole().name())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }


}
