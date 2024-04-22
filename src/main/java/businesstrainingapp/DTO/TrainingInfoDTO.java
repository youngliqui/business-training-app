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
@Schema(description = "Информация о тренинге")
public class TrainingInfoDTO {
    @Schema(description = "Название")
    private String title;
    @Schema(description = "Имя тренера")
    private String trainerName;
    @Schema(description = "Дата проведения")
    private String date;
    @Schema(description = "Отрасль")
    private String field;
}
