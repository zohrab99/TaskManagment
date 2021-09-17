package com.company.task.helper;

import com.company.task.dao.entity.User;
import com.company.task.dao.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserHelper {

    private final UserRepository userRepository;

    public UserHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Integer id){
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
