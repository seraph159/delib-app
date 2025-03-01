package com.delibrary.lib_backend.exception;

public class ClientNotFoundException extends RuntimeException {


    public ClientNotFoundException(String clientEmail) {

        super("Client not found with email: " + clientEmail);
    }
}