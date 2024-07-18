package com.practice.Rentread.Exceptions;

public class UserExistException extends RuntimeException{

    public UserExistException(){
        super();
    }

    public UserExistException(String msg){
        super(msg);
    }
}
