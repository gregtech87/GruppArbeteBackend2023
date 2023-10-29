package com.Grupparbete.API.exceptions;

public class McBookingNotFoundException extends RuntimeException{

    public McBookingNotFoundException(){

    }
    public McBookingNotFoundException(String message){
        super(message);
    }

    public McBookingNotFoundException(String message, Throwable cause){
        super(message, cause);
    }
    public McBookingNotFoundException(Throwable cause){
        super(cause);
    }
}
