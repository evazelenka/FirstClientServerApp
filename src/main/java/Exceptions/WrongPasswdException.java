package Exceptions;

public class WrongPasswdException extends RuntimeException {
    public WrongPasswdException(String msg){
        super(msg);
    }
}
