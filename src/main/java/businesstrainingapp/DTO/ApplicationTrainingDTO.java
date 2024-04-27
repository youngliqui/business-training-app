package businesstrainingapp.DTO;

import businesstrainingapp.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Заявление на проведение тренинга")
public class ApplicationTrainingDTO {
    @Schema(description = "Тема тренинга")
    private String topic;

    @Schema(description = "Дата и время проведения")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private String dateTime;

    @Schema(description = "Описание тренинга")
    private String description;

    @JsonIgnore
    private User trainer;
}
