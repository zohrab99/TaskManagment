package com.company.task.dao.repository;

import com.company.task.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
