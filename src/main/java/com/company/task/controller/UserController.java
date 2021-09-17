package com.company.task.controller;

import com.company.task.model.dto.UserDto;
import com.company.task.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Object> add(@Valid @RequestBody UserDto userDto){
        log.debug("/users , userDto = {}",userDto);
       userService.add(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}