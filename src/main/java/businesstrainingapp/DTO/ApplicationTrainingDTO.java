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
@Schema(description = "Заявление на проведение треннинга")
public class ApplicationTrainingDTO {
    @Schema(description = "Название тренинга")
    private String title;

    @Schema(description = "Дата проведения")
    private String date;

    @Schema(description = "Описание тренинга")
    private String description;
}
