package com.delibrary.lib_backend.exception;

public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(String documentId) {

        super("Document not found with id: " + documentId);
    }
}