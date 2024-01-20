package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.TaskUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRoleRepository extends JpaRepository<TaskUserRole, Long> {

}
