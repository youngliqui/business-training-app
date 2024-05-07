package businesstrainingapp.servicesImpl;

import businesstrainingapp.DTO.*;
import businesstrainingapp.exceptions.PasswordNotMatchException;
import businesstrainingapp.exceptions.UserNotFoundException;
import businesstrainingapp.mappers.TrainingMapper;
import businesstrainingapp.mappers.UserMapper;
import businesstrainingapp.models.Enums.Role;
import businesstrainingapp.models.Image;
import businesstrainingapp.models.Training;
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
                .isBlocked(false)
                .ratesAmount(0)
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
        return UserMapper.USER_MAPPER.toListUserInfoDTO(userRepository.findAll());
    }

    @Override
    public ProfileUserDTO getUserProfileById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id - " + id + " was not found")
        );

        return UserMapper.USER_MAPPER.toProfileUserDTO(user);
    }

    @Override
    public ProfileUserDTO getUserProfileByName(String name) {
        User user = userRepository.findByName(name).orElseThrow(
                () -> new UserNotFoundException("user with name - " + name + " was not found")
        );

        return UserMapper.USER_MAPPER.toProfileUserDTO(user);
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

        return UserMapper.USER_MAPPER.toProfileUserDTO(user);
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
        }
        imageRepository.save(image);

        user.setProfileImage(image);
        userRepository.save(user);

        return UserMapper.USER_MAPPER.toProfileUserDTO(user);
    }

    @Override
    @Transactional
    public ProfileUserDTO updateProfileDescription(String username, String description) {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with username - " + username + " was not found")
        );

        user.setDescription(description);
        userRepository.save(user);

        return UserMapper.USER_MAPPER.toProfileUserDTO(user);
    }

    @Override
    public UserInfoDTO getUserInfoById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("user with id - " + id + " was not found")
        );

        return UserMapper.USER_MAPPER.toUserInfoDTO(user);
    }

    @Override
    @Transactional
    public void blockUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user with id - " + userId + " was not found")
        );

        if (!user.getIsBlocked()) {
            user.setIsBlocked(true);
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void unblockUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user with id - " + userId + " was not found")
        );

        if (user.getIsBlocked()) {
            user.setIsBlocked(false);
            userRepository.save(user);
        }
    }

    @Override
    public List<UserInfoDTO> getBlockedUsers() {
        return UserMapper.USER_MAPPER.toListUserInfoDTO(userRepository.findUsersByIsBlockedTrue());
    }


    @Override
    @Transactional
    public void ratingUser(Long userId, Integer rating, String trainerName) {
        if (!(rating <= 5 && rating >= 1)) {
            throw new IllegalArgumentException("rating should be from 0 to 5");
        }

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("user with id - " + userId + " was not found")
        );

        User trainer = userRepository.findByName(trainerName).orElseThrow(
                () -> new UserNotFoundException("user with name - " + trainerName + " was not found")
        );

        if (!userRepository.existsByTrainingsTrainerIdAndUserId(userId, trainer.getId())) {
            throw new UserNotFoundException("training with trainer - " + trainerName +
                    " was not found for user with id " + userId
            );
        }

        user.addRate(rating);

        userRepository.save(user);
    }

    @Override
    public ResponseRole getUserRoleByUsername(String username) {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with name - " + username + " was not found")
        );

        return ResponseRole.builder()
                .role(user.getRole().name())
                .build();
    }

    @Override
    public List<TrainingInfoDTO> getActiveTrainings(String username) {
        User user = userRepository.findByName(username).orElseThrow(
                () -> new UserNotFoundException("user with name - " + username + " was not found")
        );

        return TrainingMapper.TRAINING_MAPPER.toListTrainingInfoDTO(user.getUserTrainings().stream()
                .filter(Training::isAvailable)
                .collect(Collectors.toList())
        );
    }
}
