package com.example.buysell.models.TaskPackage;

import com.example.buysell.models.UserPackage.User;
import com.example.buysell.repositories.*;
import com.example.buysell.services.TaskService;
import com.example.buysell.services.UserService;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class TaskCreationDtoTest {

    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;


    /**
     * Тест для ф-ии isCorrectInput (Завершен)
     */
    @Test
    void isCorrectInput() {


        //Проверка, при корректных значениях
        TaskCreationDto taskCreationDto = createCorrectObject();
        int sizeAccesses = taskCreationDto.getAccesses().size();

        Assertions.assertTrue(taskCreationDto.isCorrectInput());
        Assertions.assertTrue(taskCreationDto.getAccesses().size() == sizeAccesses);
        Assertions.assertTrue(taskCreationDto.getInfo().equals(""));

        //Проверка при частично корректных значениях

        taskCreationDto = createCorrectObject();
        taskCreationDto.getAccesses().get(0).setRole(null);
        taskCreationDto.getAccesses().get(1).setUser(null);
        sizeAccesses = taskCreationDto.getAccesses().size();

        Assertions.assertTrue(taskCreationDto.isCorrectInput());
        Assertions.assertTrue(taskCreationDto.getAccesses().size() == sizeAccesses - 2);
        Assertions.assertTrue(taskCreationDto.getInfo().equals(""));

        //Проверка при не корректных значениях 1
        taskCreationDto = createCorrectObject();
        taskCreationDto.getAccesses().get(0).setRole(null);
        taskCreationDto.getAccesses().get(1).setUser(null);
        taskCreationDto.getAccesses().get(2).setUser(null);

        Assertions.assertFalse(taskCreationDto.isCorrectInput());
        Assertions.assertTrue(taskCreationDto.getAccesses().size() == 0);
        Assertions.assertFalse(taskCreationDto.getInfo().equals(""));

        //Проверка при не корректных значениях 2
        taskCreationDto = createCorrectObject();
        taskCreationDto.setAccesses(null);

        Assertions.assertFalse(taskCreationDto.isCorrectInput());
        Assertions.assertFalse(taskCreationDto.getInfo().equals(""));

        //Проверка при не корректных значениях
        taskCreationDto = createCorrectObject();
        taskCreationDto.getTask().setTitle("");

        Assertions.assertFalse(taskCreationDto.isCorrectInput());
        Assertions.assertFalse(taskCreationDto.getInfo().equals(""));

        //Проверка при не корректных значениях 4
        taskCreationDto = createCorrectObject();
        taskCreationDto.getTask().setDescription("");

        Assertions.assertFalse(taskCreationDto.isCorrectInput());
        Assertions.assertFalse(taskCreationDto.getInfo().equals(""));

        //Проверка при не корректных значениях 5
        taskCreationDto = createCorrectObject();
        taskCreationDto.getTask().setStatus(null);

        Assertions.assertFalse(taskCreationDto.isCorrectInput());
        Assertions.assertFalse(taskCreationDto.getInfo().equals(""));

        //Проверка при не корректных значениях 6
        taskCreationDto = createCorrectObject();
        taskCreationDto.setTask(null);

        Assertions.assertFalse(taskCreationDto.isCorrectInput());
        Assertions.assertFalse(taskCreationDto.getInfo().equals(""));

        //Проверка при не корректных значениях 7
        taskCreationDto = createCorrectObject();
        taskCreationDto.setAccesses(null);

        Assertions.assertFalse(taskCreationDto.isCorrectInput());
        Assertions.assertFalse(taskCreationDto.getInfo().equals(""));

        //Проверка при не корректных значениях 8
        taskCreationDto = createCorrectObject();
        sizeAccesses = taskCreationDto.getAccesses().size();

        taskCreationDto.getAccesses().addAll(createCorrectObject().getAccesses());



        Assertions.assertTrue(taskCreationDto.isCorrectInput());
        Assertions.assertTrue(taskCreationDto.getAccesses().size() == sizeAccesses);
        Assertions.assertTrue(taskCreationDto.getInfo().equals(""));





    }

    /**
     * Создание корректного объекта
     */
    public TaskCreationDto createCorrectObject(){
        List<User> userList = userService.list();

        List<TaskStatus> taskStatusList = taskService.listStatusesTasks();
        List<TaskUserRole> taskUserRoles = taskService.listUserRolesToTask();

        Task task = new Task();
        task.setTitle("title1");
        task.setDescription("description1");
        task.setStatus(taskStatusList.get(4));


        List<TaskAccess> listAccesses = new ArrayList<>();
        TaskAccess access = new TaskAccess();
        access.setRole(taskUserRoles.get(2));
        access.setUser(userList.get(1));
        listAccesses.add(access);

        access = new TaskAccess();
        access.setRole(taskUserRoles.get(1));
        access.setUser(userList.get(2));
        listAccesses.add(access);

        access = new TaskAccess();
        access.setRole(taskUserRoles.get(0));
        access.setUser(userList.get(2));
        listAccesses.add(access);

        TaskCreationDto taskCreationDto = new TaskCreationDto();
        taskCreationDto.setTask(task);
        taskCreationDto.setAccesses(listAccesses);

        return taskCreationDto;
    }


}