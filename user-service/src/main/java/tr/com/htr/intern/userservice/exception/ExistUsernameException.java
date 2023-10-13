package tr.com.htr.intern.userservice.exception;

public class ExistUsernameException extends RuntimeException{
    public ExistUsernameException(String message){
        super(message);
    }
}
