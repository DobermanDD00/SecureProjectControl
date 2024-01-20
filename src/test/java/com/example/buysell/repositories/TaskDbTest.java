package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.TaskActive;
import com.example.buysell.models.TaskPackage.TaskStatus;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskDbTest {

    @Test
    void toTaskDb() {
        User user = new User();
        User lead = new User();
        User performer = new User();
        TaskStatus status = new TaskStatus();
        TaskActive active = new TaskActive();

        Task task = new Task();
        task.setId(4535L);
        task.setTitle("sldfh");
        task.setDescription("sldjfk");

        task.setStatus(status);
        task.setActive(active);
        task.setHistory("sdlkjfs;dk");

        System.out.println(task.toString());
        System.out.println();
        System.out.println((new TaskDb(task, user)).toTask(user));


        Assertions.assertTrue(task.equals(new TaskDb(task, user).toTask(user)));

    }




}