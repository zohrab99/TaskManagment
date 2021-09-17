package com.company.task.mapper;

import com.company.task.dao.entity.User;
import com.company.task.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(target="role.id", source = "roleId"),
            @Mapping(target="password", source = "password")
    })
    User toEntity(UserDto userDto);
}
