package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

}
