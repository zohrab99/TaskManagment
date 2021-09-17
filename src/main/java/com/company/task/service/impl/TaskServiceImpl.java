package com.company.task.service.impl;

import com.company.task.dao.entity.Task;
import com.company.task.dao.entity.User;
import com.company.task.dao.repository.TaskRepository;
import com.company.task.exception.CustomNotFoundException;
import com.company.task.exception.CustomNotPermittedException;
import com.company.task.helper.UserHelper;
import com.company.task.mapper.TaskMapper;
import com.company.task.model.dto.TaskDto;
import com.company.task.model.enums.TaskStatus;
import com.company.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.task.model.enums.ResponseEnum.NOT_ALLOWED;
import static com.company.task.model.enums.ResponseEnum.NOT_FOUND;
import static com.company.task.model.enums.RoleEnum.STUDENT;
import static com.company.task.model.enums.TaskStatus.IS_EXECUTING;

@Service
@Slf4j
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserHelper userHelper;

    public TaskServiceImpl(TaskRepository taskRepository, UserHelper userHelper) {
        this.taskRepository = taskRepository;
        this.userHelper = userHelper;
    }

    @Value("${value.defaultRank}")
    private Integer defaultRank;

    @Value("${value.criticalRank}")
    private Integer criticalRank;

    @Override
    public void add(TaskDto taskDto, String username) {
        User assignedTo = userHelper.findById(taskDto.getAssignedToValue())
                .orElseThrow(() -> new CustomNotFoundException
                        (NOT_FOUND.getMessage() + taskDto.getAssignedToValue(), NOT_FOUND.getStatus()));

        User assignedBy = userHelper.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException
                        (NOT_FOUND.getMessage() + username, NOT_FOUND.getStatus()));

        if (!taskDto.getAssignedToValue().equals(STUDENT.getRole()))
            throw new CustomNotPermittedException
                    (NOT_ALLOWED.getMessage() + taskDto.getAssignedToValue(), NOT_ALLOWED.getStatus());

        if(taskRepository.countByAssignedToAndRank(assignedTo,criticalRank)>0)
            throw new CustomNotPermittedException
                    (NOT_ALLOWED.getMessage() + taskDto.getAssignedToValue(), NOT_ALLOWED.getStatus());

        taskRepository.save(TaskMapper.INSTANCE.toEntity
                (taskDto.getContent(), defaultRank, taskDto.getAssignedToValue(),
                        assignedBy.getId(), taskDto.getDeadline(), IS_EXECUTING));
    }

    @Override
    public List<TaskDto> getListByUserId(Integer studentId) {
        User user = userHelper.findById(studentId)
                .orElseThrow(() -> new CustomNotFoundException
                        (NOT_FOUND.getMessage() + studentId, NOT_FOUND.getStatus()));

        return taskRepository.findAllByAssignedTo(user).stream()
                .map(TaskMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void completeStatus(Integer taskId, String username) {
        User user = userHelper.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException
                        (NOT_FOUND.getMessage() + username, NOT_FOUND.getStatus()));
        Task task = taskRepository.findByIdAndAssignedTo(taskId, user)
                .orElseThrow(() -> new CustomNotFoundException
                        (NOT_FOUND.getMessage() + taskId, NOT_FOUND.getStatus()));
        if (task.getStatus().equals(IS_EXECUTING.ordinal())) {
            task.setStatus(TaskStatus.SUCCESS.ordinal());
            Date currentDate = new Date();
            if (currentDate.after(task.getDeadline()))
                task.setRank(task.getRank() - 1);
        }
        taskRepository.save(task);
    }

    @Override
    public List<TaskDto> getList(String username) {
        User user = userHelper.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException
                        (NOT_FOUND.getMessage() + username, NOT_FOUND.getStatus()));

        return taskRepository.findAllByAssignedBy(user).stream()
                .map(TaskMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
}
