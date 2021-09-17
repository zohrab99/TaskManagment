package com.company.task.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    @JsonIgnore
    private Integer id;

    @NotBlank(message = "{NotBlank.user.username}")
    private String username;

    @NotBlank(message = "{NotBlank.user.password}")
    private String password;

    @NotBlank(message = "{NotBlank.user.name}")
    private String name;

    @NotBlank(message = "{NotBlank.user.surname}")
    private String surname;

    @NotNull(message = "{NotNull.user.age")
    @Min(value = 10, message = "{Min.user.age")
    @Max(value = 150, message = "{Max.user.age")
    private Integer age;

    @NotNull(message = "{NotNull.user.roleId")
    private Integer roleId;

}