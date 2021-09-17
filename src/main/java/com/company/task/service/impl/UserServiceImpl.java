package com.company.task.service.impl;

import com.company.task.dao.repository.UserRepository;
import com.company.task.exception.CustomAlreadyExistsException;
import com.company.task.exception.CustomNotFoundException;
import com.company.task.helper.RoleHelper;
import com.company.task.mapper.UserMapper;
import com.company.task.model.dto.UserDto;
import com.company.task.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.company.task.model.enums.ResponseEnum.ALREADY_EXISTS;
import static com.company.task.model.enums.ResponseEnum.NOT_FOUND;

@Service
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleHelper roleHelper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleHelper roleHelper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleHelper = roleHelper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void add(UserDto userDto) {
        roleHelper.findById(userDto.getRoleId())
                .orElseThrow(() -> new CustomNotFoundException
                        (NOT_FOUND.getMessage() + userDto.getRoleId(),NOT_FOUND.getStatus()));
        if (userRepository.existsByUsername(userDto.getUsername()))
            throw new CustomAlreadyExistsException
                    (ALREADY_EXISTS.getMessage() + userDto.getUsername(),ALREADY_EXISTS.getStatus());

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(UserMapper.INSTANCE.toEntity(userDto));
    }
}