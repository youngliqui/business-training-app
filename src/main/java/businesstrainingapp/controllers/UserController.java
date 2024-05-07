package businesstrainingapp.controllers;

import businesstrainingapp.DTO.*;
import businesstrainingapp.exceptions.UserNotAuthorizeException;
import businesstrainingapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "методы для работы с пользователями")
@SecurityRequirement(name = "basicAuth")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Получение информации о всех пользователях")
    public List<UserInfoDTO> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/user-role")
    @Operation(summary = "Получение роли пользователя")
    public ResponseRole getUserRole(Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("You are not authorize");
        }

        return userService.getUserRoleByUsername(principal.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Получение информации о пользователе по id")
    public UserInfoDTO getUserById(@PathVariable("id") Long id) {
        return userService.getUserInfoById(id);
    }

    @PostMapping("/new-user")
    @Operation(summary = "Регистранция нового пользователя")
    public ResponseEntity<Void> addUser(@RequestBody @Valid RegistrationUserDTO userDTO) {
        userService.addUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/profile")
    @Operation(summary = "Просмотр профиля пользователя")
    public ProfileUserDTO profileUser(Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("You are not authorize");
        }

        return userService.getUserProfileByName(principal.getName());
    }

    @PutMapping("/profile")
    @Operation(summary = "Обновление информации в профиле пользователя")
    public ProfileUserDTO updateProfileUser(Principal principal,
                                            @RequestBody UpdationUserDTO userDTO) {
        if (principal == null) {
            throw new UserNotAuthorizeException("You are not authorize");
        }

        return userService.updateUserProfileByName(principal.getName(), userDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Удаление пользователя по id")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long userId) {
        userService.deleteUserById(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/username/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Удаление пользователя по username")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable("username") String username) {
        userService.deleteUserByName(username);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/profile/image")
    @Operation(summary = "Изменения фотографии профиля пользователя")
    public ProfileUserDTO updateProfileImage(@RequestPart MultipartFile file, Principal principal) {
        try {
            if (principal == null) {
                throw new UserNotAuthorizeException("you are not authorize");
            }

            return userService.addProfileImage(principal.getName(), file);

        } catch (IOException e) {
            throw new RuntimeException("image was not saved");
        }
    }

    @PostMapping("/profile/description")
    @Operation(summary = "Изменение описания профиля пользователя")
    public ProfileUserDTO updateProfileDescription(@RequestBody ProfileDescriptionRequest descriptionRequest,
                                                   Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        return userService.updateProfileDescription(principal.getName(), descriptionRequest.getDescription());
    }

    private static class ProfileDescriptionRequest {
        private String description;

        public String getDescription() {
            return description;
        }
    }

    @PatchMapping("/{id}/block")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Блокирование пользователя по id")
    public ResponseEntity<Void> blockUserById(@PathVariable("id") Long userId) {
        userService.blockUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}/unblock")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Разблокирование пользователя по id")
    public ResponseEntity<Void> unblockUserById(@PathVariable("id") Long userId) {
        userService.unblockUserById(userId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/blocks")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Получение всех заблокированных пользователей")
    public List<UserInfoDTO> getAllBlockedUsers() {
        return userService.getBlockedUsers();
    }

    @PostMapping("/{id}/rating")
    @PreAuthorize("hasAuthority('TRAINER')")
    @Operation(summary = "выставление оценки пользователю")
    public ResponseEntity<Void> rateUser(@PathVariable("id") Long userId, @RequestBody Map<String, Integer> rating,
                                         Principal principal) {

        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        userService.ratingUser(userId, rating.get("rate"), principal.getName());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/trainer")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Функция \"стать тренером\"")
    public ResponseEntity<Void> becomeTrainer(Principal principal, @RequestBody CoachBecomingDTO coachBecomingDTO) {

        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        // написать логику функции заявки пользователя, чтобы стать тренером

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/trainings/active")
    @Operation(summary = "Получение списка активных тренингов пользователя")
    @PreAuthorize("hasAuthority('USER')")
    public List<TrainingInfoDTO> getActiveTrainingsForUser(Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        return userService.getActiveTrainings(principal.getName());
    }
}
