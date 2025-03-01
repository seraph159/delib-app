package com.delibrary.lib_backend.exception;

public class CustomBlobStorageException extends RuntimeException {

    public CustomBlobStorageException(String message, Throwable cause) {
        super(message, cause);
    }

}