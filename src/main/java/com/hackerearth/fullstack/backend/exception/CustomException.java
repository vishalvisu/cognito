package com.hackerearth.fullstack.backend.exception;

public class CustomException extends Exception{
    String message;
    int code;
    public CustomException(String message, int code)
    {
        this.message=message;
        this.code=code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
