package com.study.self.spring.boot.jpa.exception;


public class DataBaseException extends RuntimeException {

    public DataBaseException(String message, Exception exception){
        super(message, exception);
    }
}
