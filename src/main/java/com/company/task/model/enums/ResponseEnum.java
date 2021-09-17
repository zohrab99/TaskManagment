package com.company.task.model.enums;

import lombok.Getter;

@Getter
public enum ResponseEnum {

    INVALID_AUTHENTICATION(401, "Bad credentials"),
    AUTH_TOKEN_EXPIRED(401, "Token is expired"),
    INVALID_JWT_TOKEN(401, "Invalid token"),
    AUTHENTICATION_REQUIRED_EXCEPTION(401, "Authentication is required"),
    ACCESS_DENIED(403, "Access is denied"),
    SUCCESS_AUTHENTICATION(200, "Successful authentication"),
    NOT_FOUND(404, "Not-found: "),
    NOT_ALLOWED(405, "Not-allowed: "),
    NOT_READABLE(400, "Not-readable: "),
    ALREADY_EXISTS(409, "Already exists: "),;


    private int status;
    private String  message;

    ResponseEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
