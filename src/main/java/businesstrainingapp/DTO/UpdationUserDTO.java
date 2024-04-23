package businesstrainingapp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Обновление профиля пользователя")
public class UpdationUserDTO {
    @Schema(description = "Новое имя пользователя")
    private String username;

    @Schema(description = "Новая эл. почта пользователя")
    private String email;

    @Schema(description = "Текущий пароль")
    private String currentPassword;
    @Schema(description = "Новый пароль")
    private String newPassword;

    @Schema(description = "Подтверждение нового пароля")
    private String matchingNewPassword;
}
