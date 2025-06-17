package Exceptions;

public class WrongServerException extends RuntimeException{
    public WrongServerException(String msg){
        super(msg);
    }
}
