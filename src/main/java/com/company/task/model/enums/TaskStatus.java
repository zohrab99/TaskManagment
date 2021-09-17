package com.company.task.model.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
    IS_EXECUTING("isExecuting"),
    SUCCESS("success");

    private String status;

    TaskStatus(String status){
        this.status=status;
    }
}
