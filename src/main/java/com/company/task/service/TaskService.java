package com.company.task.service;

import com.company.task.model.dto.TaskDto;

import java.util.List;

public interface TaskService {
    void add(TaskDto taskDto, String name);

    List<TaskDto> getListByUserId(Integer studentId);

    void completeStatus(Integer taskId, String name);

    List<TaskDto> getList(String name);
}
