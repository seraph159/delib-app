package com.delibrary.lib_backend.exception;


public class InvalidCreditCardException extends RuntimeException {

    public InvalidCreditCardException(String message) {
        super(message);
    }
}