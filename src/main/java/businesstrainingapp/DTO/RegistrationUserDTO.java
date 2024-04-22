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
@Schema(description = "Регистрация пользователя")
public class RegistrationUserDTO {

    @Schema(description = "Имя пользователя")
    private String name;
    @Schema(description = "Эл. почта пользователя")
    private String email;
    @Schema(description = "Пароль пользователя")
    private String password;
    @Schema(description = "Роль (пользователь/тренер)")
    private String role;
}
