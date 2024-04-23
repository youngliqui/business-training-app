package businesstrainingapp.services;

import businesstrainingapp.DTO.ProfileUserDTO;
import businesstrainingapp.DTO.RegistrationUserDTO;
import businesstrainingapp.DTO.UpdationUserDTO;
import businesstrainingapp.DTO.UserInfoDTO;

import java.util.List;

public interface UserService {
    void addUser(RegistrationUserDTO userDTO);
    List<UserInfoDTO> getAll();

    ProfileUserDTO getUserProfileById(Long id);

    ProfileUserDTO getUserProfileByName(String name);

    ProfileUserDTO updateUserProfileByName(String name, UpdationUserDTO userDTO);
}
