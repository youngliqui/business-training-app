package businesstrainingapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleImageNotFoundException(ImageNotFoundException ex) {
        return new ResponseEntity<>(responseBody(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody(ex));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(responseBody(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BlockedUserLoginException.class)
    public ResponseEntity<ErrorResponse> handleBlockedUserException(BlockedUserLoginException ex) {
        return new ResponseEntity<>(responseBody(ex), HttpStatus.FORBIDDEN);
    }

    private ErrorResponse responseBody(Exception ex) {
        return new ErrorResponse(
                ex.getMessage(),
                System.currentTimeMillis()
        );
    }
}
