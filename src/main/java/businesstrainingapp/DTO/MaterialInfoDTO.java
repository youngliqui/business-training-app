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
@Schema(description = "Информация о материалах тренинга")
public class MaterialInfoDTO {
    @Schema(description = "id материала")
    private Long id;
    @Schema(description = "ссылка на материал")
    private String link;
    @Schema(description = "Название файла")
    private String filename;

    @Schema(description = "Описание к файлу")
    private String description;
}
