package businesstrainingapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAuthorizeException extends RuntimeException {
    public UserNotAuthorizeException(String message) {
        super(message);
    }
}
