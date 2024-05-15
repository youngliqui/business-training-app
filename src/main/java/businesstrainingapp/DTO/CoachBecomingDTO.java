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
@Schema(description = "Форма на становление тренера")
public class CoachBecomingDTO {
    @Schema(description = "Имя тренера")
    private String trainerName;
}
