package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<Status, Long> {

}
