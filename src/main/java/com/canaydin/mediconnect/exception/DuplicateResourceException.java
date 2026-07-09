package com.canaydin.mediconnect.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(resourceName + " already exists with " + fieldName + ": " + fieldValue);
    }
}
