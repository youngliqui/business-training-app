package businesstrainingapp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Профиль пользователя")
public class ProfileUserDTO {
    @Schema(description = "Информация о пользователе")
    private String description;
    @Schema(description = "Архив тренингов")
    private List<String> trainingArchive;
    @Schema(description = "Рейтинг пользователя")
    private Integer rating;
}
