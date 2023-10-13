package tr.com.htr.intern.userservice.exception;

public class AuthenticateException extends RuntimeException{
    public AuthenticateException(String message){
        super(message);
    }
}
