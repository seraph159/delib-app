package com.delibrary.lib_backend.exception;

public class DocumentNotAvailableException extends RuntimeException {

    public DocumentNotAvailableException(String documentId) {
        super("Document with id: " + documentId + " is not available for borrowing");
    }
}
