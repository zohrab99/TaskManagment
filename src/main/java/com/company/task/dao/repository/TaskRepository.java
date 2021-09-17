package com.company.task.dao.repository;

import com.company.task.dao.entity.Task;
import com.company.task.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findAllByAssignedTo(User assignedTo);

    List<Task> findAllByAssignedBy(User assignedBy);

    Optional<Task> findByIdAndAssignedTo(Integer id,User user);

    int countByAssignedToAndRank(User user,Integer rank);
}
