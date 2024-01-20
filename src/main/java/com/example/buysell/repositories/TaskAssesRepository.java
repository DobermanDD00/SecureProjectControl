package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.TaskAccess;
import com.example.buysell.models.TaskPackage.TaskActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface TaskAssesRepository extends JpaRepository<TaskAccess, Long> {
    List<TaskAccess> findByTaskDbId(long id);
    void deleteByTaskDbId(long id);


}
