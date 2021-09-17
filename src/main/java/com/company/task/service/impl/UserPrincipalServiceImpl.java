package com.company.task.service.impl;

import com.company.task.dao.entity.User;
import com.company.task.dao.repository.UserRepository;
import com.company.task.exception.CustomNotFoundException;
import com.company.task.service.UserPrincipalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.company.task.model.enums.ResponseEnum.INVALID_AUTHENTICATION;
import static com.company.task.model.enums.ResponseEnum.NOT_FOUND;

@Slf4j
@Service
@Transactional
public class UserPrincipalServiceImpl implements UserPrincipalService {

    private final UserRepository userRepository;

    public UserPrincipalServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getRoleNameByUsername(String username) {
        log.debug("username: {}",username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException
                        (NOT_FOUND.getMessage() + username, NOT_FOUND.getStatus()));
        return user.getRole().getName();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserPrincipalService: loadUserByUsername({})", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_AUTHENTICATION.getMessage()));
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(), user.getPassword(), authorities);
    }

}
