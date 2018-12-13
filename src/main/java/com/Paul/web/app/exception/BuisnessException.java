package com.Paul.web.app.exception;


public class BuisnessException extends RuntimeException{

    public BuisnessException() {
        super();
    }

    public BuisnessException(String message) {
        super(message);
    }

    public BuisnessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuisnessException(Throwable cause) {
        super(cause);
    }

    protected BuisnessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

