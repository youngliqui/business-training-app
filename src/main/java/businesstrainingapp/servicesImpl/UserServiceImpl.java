package businesstrainingapp.servicesImpl;

import businesstrainingapp.DTO.ProfileUserDTO;
import businesstrainingapp.DTO.RegistrationUserDTO;
import businesstrainingapp.DTO.UpdationUserDTO;
import businesstrainingapp.DTO.UserInfoDTO;
import businesstrainingapp.exceptions.PasswordNotMatchException;
import businesstrainingapp.exceptions.UserNotFoundException;
import businesstrainingapp.models.Enums.Role;
import businesstrainingapp.models.User;
import businesstrainingapp.repositories.UserRepository;
import businesstrainingapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Component
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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

        if (userDTO.getRole().equals("speaker")) {
            user.setRole(Role.SPEAKER);
        }
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
                .description(user.getDescription())
                .image(user.getProfileImage())
                .rating(user.getRating())
                .trainingArchive(user.getTrainings().stream()
                        .map(training -> training.getTitle() + " - " + training.getDate())
                        .toList())
                .build();
    }
}
