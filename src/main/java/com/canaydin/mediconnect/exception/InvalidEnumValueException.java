package com.canaydin.mediconnect.exception;

public class InvalidEnumValueException extends RuntimeException {

    public InvalidEnumValueException(String fieldName , String value) {
        super("Invalid value for " + fieldName + ": " + value);
    }
}
