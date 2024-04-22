package businesstrainingapp.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageSaveResult {
    private boolean error;
    private String filename;
    private String link;
}
