package com.company.task.controller;

import com.company.task.model.dto.TaskDto;
import com.company.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public void add(@Valid @RequestBody TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("/tasks , taskDto = {} , assignedBy = {}", taskDto, authentication.getName());
        taskService.add(taskDto, authentication.getName());
    }

    @GetMapping
    public List<TaskDto> getList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("/tasks , assignedBy = {}", authentication.getName());
        return taskService.getList(authentication.getName());
    }

    @GetMapping("/users/{studentId}")
    public List<TaskDto> getListByUserId(@PathVariable Integer studentId) {
        log.debug("/tasks/users/{}", studentId);
        return taskService.getListByUserId(studentId);
    }

    @PutMapping("/complete-status/{taskId}")
    public void completeStatus(@PathVariable Integer taskId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("/tasks/complete-status/{} , username = {}", taskId,authentication.getName());
        taskService.completeStatus(taskId,authentication.getName());
    }
}
