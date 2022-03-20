package com.laioffer.airbnb.exception;

public class InvalidReservationDateException extends RuntimeException{
    public InvalidReservationDateException(String message) {
        super(message);
    }
}
