package com.company.task.service;

import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserPrincipalService extends UserDetailsService {

    String getRoleNameByUsername(String username);

}
