package tr.com.htr.intern.userservice.exception;

public class UserCouldNotBeCreatedException extends RuntimeException {
    public UserCouldNotBeCreatedException(String message) {
        super(message);
    }
}
