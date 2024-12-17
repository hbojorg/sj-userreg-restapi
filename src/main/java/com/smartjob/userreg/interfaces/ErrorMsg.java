package com.smartjob.userreg.interfaces;

public record ErrorMsg(String message, Integer statusCode) {

    public static ErrorMsg instance(String message, Integer statusCode) {
        return new ErrorMsg(message, statusCode);
    }
}
