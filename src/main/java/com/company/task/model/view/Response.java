package com.company.task.model.view;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Response<T> {
    private int status;
    private String message;
    private T data;
}
