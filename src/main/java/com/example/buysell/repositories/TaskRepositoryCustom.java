package com.example.buysell.repositories;

import com.example.buysell.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepositoryCustom {
    List<Task> findByTitle(String title);


}
