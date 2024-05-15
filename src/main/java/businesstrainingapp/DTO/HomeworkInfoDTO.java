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
@Schema(description = "Информация о домашнем задании")
public class HomeworkInfoDTO {
    @Schema(description = "id домашнего задания")
    private Long id;
    @Schema(description = "ссылка на домашнее задание")
    private String link;

    @Schema(description = "домашнее задание")
    private String filename;
    @Schema(description = "имя пользователя")
    private String username;
}
