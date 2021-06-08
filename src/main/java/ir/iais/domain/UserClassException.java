package ir.iais.domain;

/**
 * @author vahid
 * create on 6/6/2021
 */
public class UserClassException extends RuntimeException {

    public UserClassException() {
    }

    public UserClassException(String message) {
        super(message);
    }

    public UserClassException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserClassException(Throwable cause) {
        super(cause);
    }

    public UserClassException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}