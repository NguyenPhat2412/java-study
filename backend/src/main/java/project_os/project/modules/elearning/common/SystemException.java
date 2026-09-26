package project_os.project.modules.elearning.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class SystemException extends ResponseStatusException {

    public SystemException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public SystemException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public SystemException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public static SystemException notFound(String message) {
        return new SystemException(HttpStatus.NOT_FOUND, message);
    }

    public static SystemException badRequest(String message) {
        return new SystemException(HttpStatus.BAD_REQUEST, message);
    }

    public static SystemException internal(String message) {
        return new SystemException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
