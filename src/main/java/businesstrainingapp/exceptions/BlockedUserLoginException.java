package businesstrainingapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BlockedUserLoginException extends RuntimeException {
    public BlockedUserLoginException(String message) {
        super(message);
    }
}
