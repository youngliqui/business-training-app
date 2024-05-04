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
@Schema(description = "Информация о тренинге для тренера")
public class TrainingInfoForTrainerDTO {
    @Schema(description = "id тренинга")
    private Long id;
    @Schema(description = "тема тренинга")
    private String topic;

    @Schema(description = "отрасль тренинга")
    private String branch;
    @Schema(description = "дата начала")
    private String dateTime;
    @Schema(description = "кол-во записаных пользователей")
    private Integer usersAmount;
    @Schema(description = "Финансы")
    private String finance;
    @Schema(description = "Мест осталось")
    private Integer seatsLeft;
    @Schema(description = "Материалы")
    private List<MaterialInfoDTO> materials;
}
