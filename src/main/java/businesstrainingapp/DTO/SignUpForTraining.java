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
@Schema(description = "Запись на тренинг")
public class SignUpForTraining {
    @Schema(description = "Имя пользователя")
    private String name;

    @Schema(description = "Эл. почта пользователя")
    private String email;
}
