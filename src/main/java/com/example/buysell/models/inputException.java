package com.example.buysell.models;

public class inputException extends Exception{
    public inputException() {
    }

    public inputException(String message) {
        super(message);
    }

    public inputException(String message, Throwable cause) {
        super(message, cause);
    }

    public inputException(Throwable cause) {
        super(cause);
    }

    public inputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
