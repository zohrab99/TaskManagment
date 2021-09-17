package com.company.task.exception;

import lombok.Getter;

@Getter
public class CustomAlreadyExistsException extends RuntimeException{

    private final Integer status;

    public CustomAlreadyExistsException(String message, Integer status){
        super(message);
        this.status = status;
    }

}
