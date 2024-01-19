package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor


public class TaskRepositoryCustom {
    private final User user;
    private final TaskRepositoryDb taskRepositoryDb;

    public List<Task> findByTitle(String title) {
        return null;//***************************
    }

    public List<Task> findAll() {
        List<TaskDb> tasksDb = taskRepositoryDb.findAll();
        return TaskDb.toTask(tasksDb, user);
    }

    public Task save(Task task) {
        TaskDb taskDb = new TaskDb(task, user);
        TaskDb taskDbReturned = taskRepositoryDb.save(taskDb);
        return taskDbReturned.toTask(user);
    }

    public void deleteById(Long id) {
        taskRepositoryDb.deleteById(id);
    }

    public Task findById(Long id) {
        TaskDb taskDb = taskRepositoryDb.findById(id).orElse(null);
        if (taskDb == null) return null;
        return taskDb.toTask(user);
    }
}
