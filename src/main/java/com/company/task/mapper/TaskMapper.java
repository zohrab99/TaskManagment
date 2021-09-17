package com.company.task.mapper;

import com.company.task.dao.entity.Task;
import com.company.task.dao.entity.User;
import com.company.task.model.dto.TaskDto;
import com.company.task.model.enums.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper
public interface TaskMapper {
    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mappings({
            @Mapping(target="status", source = "taskStatus",qualifiedByName = "toStatusValue"),
            @Mapping(target="assignedTo.id", source = "assignedTo"),
            @Mapping(target="assignedBy.id", source = "assignedBy")
    })
    Task toEntity(String content, Integer rank, Integer assignedTo, Integer assignedBy,
                  Date deadline, TaskStatus taskStatus);

    @Mappings({
            @Mapping(target="assignedToValue", source = "assignedTo.id")
    })
    TaskDto toDto(Task task);

    @Named(value = "toStatusValue")
    default Integer toStatusValue(TaskStatus taskStatus) {
        return taskStatus.ordinal();
    }
}
