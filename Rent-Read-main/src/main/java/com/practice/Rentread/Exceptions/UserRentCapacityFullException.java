package com.practice.Rentread.Exceptions;

public class UserRentCapacityFullException extends  RuntimeException{

    public UserRentCapacityFullException(){
        super();
    }

    public UserRentCapacityFullException(String msg){
        super(msg);
    }
}
