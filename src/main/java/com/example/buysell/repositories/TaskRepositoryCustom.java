package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.UserPackage.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor


public class TaskRepositoryCustom {
    private final TaskDbRepository taskRepositoryDb;

    public List<Task> findByTitle(String title) {
        return null;//***************************
    }

    //TODO 000 Сломано, починить
//    public List<Task> findAll() {
//        List<TaskDb> tasksDb = taskRepositoryDb.findAll();
//        return TaskDb.toTask(tasksDb, user);
//    }

    //TODO **********
    //TODO 000 Сломано, починить
    @SneakyThrows
    public long save(Task task, SecretKey taskKey) {
        TaskDb taskDb = new TaskDb(task, taskKey);
        TaskDb taskDbReturned = taskRepositoryDb.save(taskDb);
        return taskDbReturned.getId();
    }


    public void deleteById(long id) {
        taskRepositoryDb.deleteById(id);
    }

    //TODO 000 Сломано, починить
    @SneakyThrows
    public Task getTaskById(long id, SecretKey taskKey) {
        TaskDb taskDb = taskRepositoryDb.findById(id).orElse(null);
        if (taskDb == null) return null;

        return taskDb.toTask(taskKey);
    }
    public TaskDb findTaskDbById(Long id){
        return taskRepositoryDb.findById(id).orElse(null);
    }

}
