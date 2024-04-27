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
public class TrainingRequestInfoDTO {
    @Schema(description = "Id заявки на тренинг")
    private Long id;
    @Schema(description = "Тема тренига")
    private String topic;
    @Schema(description = "Описание тренинга")
    private String description;
    @Schema(description = "Дата и время проведения тренинга")
    private String dateTime;
    @Schema(description = "Имя тренера")
    private String trainerName;

}
