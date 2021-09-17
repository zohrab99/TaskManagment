package com.company.task.exception;

import lombok.Getter;

@Getter
public class CustomNotPermittedException extends RuntimeException{

    private final Integer status;

    public CustomNotPermittedException(String message, Integer status){
        super(message);
        this.status = status;
    }
}
