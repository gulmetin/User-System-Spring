package tr.com.htr.intern.userservice.exception;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String message){
        super(message);
    }
}
