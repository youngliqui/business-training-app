package businesstrainingapp.services;

import businesstrainingapp.DTO.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    void addUser(RegistrationUserDTO userDTO);

    List<UserInfoDTO> getAll();

    ProfileUserDTO getUserProfileById(Long id);

    ProfileUserDTO getUserProfileByName(String name);

    ProfileUserDTO updateUserProfileByName(String name, UpdationUserDTO userDTO);

    void deleteUserById(Long id);

    void deleteUserByName(String name);

    ProfileUserDTO addProfileImage(String username, MultipartFile file) throws IOException;

    ProfileUserDTO updateProfileDescription(String username, String description);

    UserInfoDTO getUserInfoById(Long id);

    void blockUserById(Long userId);

    void unblockUserById(Long userId);

    List<UserInfoDTO> getBlockedUsers();

    void ratingUser(Long userId, Integer rating, String trainerName);

    ResponseRole getUserRoleByUsername(String username);

    List<TrainingInfoDTO> getActiveTrainings(String username);
}
