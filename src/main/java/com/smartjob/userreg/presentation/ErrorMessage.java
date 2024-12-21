package com.smartjob.userreg.presentation;

public record ErrorMessage(String message) {

    public static ErrorMessage instance(String message) {
        return new ErrorMessage(message);
    }
}
