package com.example.buysell.repositories;

import com.example.buysell.models.TaskPackage.Active;
import com.example.buysell.models.TaskPackage.Status;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskDbTest {

    @Test
    void toTaskDb() {
        User user = new User();
        User lead = new User();
        User performer = new User();
        Status status = new Status();
        Active active = new Active();

        Task task = new Task();
        task.setId(4535L);
        task.setTitle("sldfh");
        task.setDescription("sldjfk");
        task.setLead(lead);
        task.setPerformer(performer);
        task.setStatus(status);
        task.setActive(active);
        task.setHistory("sdlkjfs;dk");

        System.out.println(task.toString());
        System.out.println();
        System.out.println((new TaskDb(task, user)).toTask(user));


        Assertions.assertTrue(task.equals(new TaskDb(task, user).toTask(user)));

    }




}