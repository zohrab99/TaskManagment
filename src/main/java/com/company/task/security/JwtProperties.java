package com.company.task.security;

public interface JwtProperties {

    String JWT_SECRET = "aDsy?8ewas@ddBk3@PmdvRf1T3sat@573pEfd";
    long JWT_TOKEN_EXPIRATION_TIME = 86400 * 1000;
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

}
