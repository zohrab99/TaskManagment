package com.company.task.model.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ADMIN(1),
    STUDENT(2),
    TEACHER(3);

    private Integer role;

    RoleEnum(Integer role){
        this.role=role;
    }
}
