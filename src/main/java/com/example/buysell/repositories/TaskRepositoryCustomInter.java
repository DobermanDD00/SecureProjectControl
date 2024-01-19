package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepositoryCustomInter extends JpaRepository<Task, Long> {
    List<Task> findByTitle(String title);

    List<Task> findAll();

    Task save(Task task);

    void deleteById(Long id);

    Optional<Task> findById(Long id);

}
