package businesstrainingapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TrainingFullException extends RuntimeException {
    public TrainingFullException(String message) {
        super(message);
    }
}
