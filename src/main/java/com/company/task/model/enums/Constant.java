package com.company.task.model.enums;

import lombok.Getter;

@Getter
public enum Constant {
    NOT_READABLE("not-readable"),
    MISSING_PARAMETER("missing-parameter"),
    AUTHORIZATION("authorization"),
    AUTHENTICATION("authentication"),
    VALIDATION("validation");


    private String value;

    Constant(String value) {
        this.value = value;
    }
}
