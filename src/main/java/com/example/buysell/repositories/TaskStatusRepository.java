package com.example.buysell.repositories;

import com.example.buysell.models.TaskStatus;
import com.example.buysell.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

}
