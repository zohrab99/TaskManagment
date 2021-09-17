package com.company.task.helper;

import com.company.task.dao.entity.Role;
import com.company.task.dao.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleHelper {
    private final RoleRepository roleRepository;

    public RoleHelper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findById(Integer roleId){
        return roleRepository.findById(roleId);
    }
}
