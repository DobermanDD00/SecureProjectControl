package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.TaskAccess;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskAssesRepository extends JpaRepository<TaskAccess, Long> {


}
