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
    @Schema(description = "Id тренинга")
    private Long id;
    @Schema(description = "Тема")
    private String topic;
    @Schema(description = "Имя тренера")
    private String trainerName;
    @Schema(description = "Дата проведения")
    private String dateTime;
    @Schema(description = "Отрасль")
    private String branch;
    @Schema(description = "Описание")
    private String description;

    @Schema(description = "Кол-во мест")
    private String totalSeats;

    @Schema(description = "Цена за вход")
    private String price;
}
