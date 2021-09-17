package com.company.task.model.dto;

import com.company.task.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
@Data
public class TaskDto {
    @JsonIgnore
    private Integer id;
    @NotBlank(message = "{NotBlank.task.content}")
    private String content;
    @JsonIgnore
    private Integer rank;
    @NotNull(message = "{NotNull.task.assignedTo")
    private Integer assignedToValue;
    @JsonIgnore
    private Integer assignedByValue;
    @NotNull(message = "{NotNull.task.deadline")
    private Date deadline;
    @JsonIgnore
    private TaskStatus taskStatus;
}
