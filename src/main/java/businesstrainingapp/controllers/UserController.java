package businesstrainingapp.controllers;

import businesstrainingapp.DTO.ProfileUserDTO;
import businesstrainingapp.DTO.RegistrationUserDTO;
import businesstrainingapp.DTO.UpdationUserDTO;
import businesstrainingapp.DTO.UserInfoDTO;
import businesstrainingapp.exceptions.UserNotAuthorizeException;
import businesstrainingapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "методы для работы с пользователями")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Получение информации о всех пользователях")
    public List<UserInfoDTO> getUsers() {
        return userService.getAll();
    }

    @PostMapping("/new-user")
    @Operation(summary = "Регистранция нового пользователя")
    public ResponseEntity<Void> addUser(@RequestBody RegistrationUserDTO userDTO) {
        userService.addUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/profile")
    @Operation(summary = "Просмотр профиля пользователя")
    public ProfileUserDTO profileUser(Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        return userService.getUserProfileByName(principal.getName());
    }

    @PutMapping("/profile")
    @Operation(summary = "Обновление информации в профиле пользователя")
    public ProfileUserDTO updateProfileUser(Principal principal,
                                            @RequestBody UpdationUserDTO userDTO) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        return userService.updateUserProfileByName(principal.getName(), userDTO);
    }

}
