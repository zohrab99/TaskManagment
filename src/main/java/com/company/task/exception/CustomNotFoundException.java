package com.company.task.exception;

import lombok.Getter;

@Getter
public class CustomNotFoundException extends RuntimeException{

    private final Integer status;

    public CustomNotFoundException(String message, Integer status){
        super(message);
        this.status = status;
    }

}
