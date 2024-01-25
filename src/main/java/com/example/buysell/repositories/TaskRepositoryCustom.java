package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.Task;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor

public class TaskRepositoryCustom {
    private final TaskDbRepository taskRepositoryDb;

    public List<Task> findByTitle(String title) {
        return null;//***************************
    }



    public TaskDb save(Task task, SecretKey taskKey) {
        return taskRepositoryDb.save(new TaskDb(task, taskKey));
    }

    public long getNextId() {
        TaskDb taskDb = taskRepositoryDb.findTopByOrderByIdDesc();
        if (taskDb == null)
            return 1;
        return taskDb.getId() + 1;
    }


    public void deleteById(long id) {
        taskRepositoryDb.deleteById(id);
    }

    @SneakyThrows
    public Task getTaskById(long id, SecretKey taskKey) {
        TaskDb taskDb = taskRepositoryDb.findById(id).orElse(null);
        if (taskDb == null) return null;

        return taskDb.toTask(taskKey);
    }

    public TaskDb findTaskDbById(Long id) {
        return taskRepositoryDb.findById(id).orElse(null);
    }

}
