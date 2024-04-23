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
@Schema(description = "Логирование пользователя")
public class LogUserDTO {
    @Schema(description = "Электронная почта пользователя")
    private String email;

    @Schema(description = "Пароль пользователя")
    private String password;
}
