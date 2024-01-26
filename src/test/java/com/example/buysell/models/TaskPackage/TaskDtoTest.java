package com.example.buysell.models.TaskPackage;

import com.example.buysell.models.UserPackage.User;
import com.example.buysell.services.TaskService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
class TaskDtoTest {

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
        TaskDto taskDto = createCorrectObject();
        int sizeAccesses = taskDto.getAccesses().size();

        Assertions.assertTrue(taskDto.isCorrectInput());
        Assertions.assertTrue(taskDto.getAccesses().size() == sizeAccesses);
        Assertions.assertTrue(taskDto.getInfo().equals(""));

        //Проверка при частично корректных значениях

        taskDto = createCorrectObject();
        taskDto.getAccesses().get(0).setRole(null);
        taskDto.getAccesses().get(1).setUser(null);
        sizeAccesses = taskDto.getAccesses().size();

        Assertions.assertTrue(taskDto.isCorrectInput());
        Assertions.assertTrue(taskDto.getAccesses().size() == sizeAccesses - 2);
        Assertions.assertTrue(taskDto.getInfo().equals(""));

        //Проверка при не корректных значениях 1
        taskDto = createCorrectObject();
        taskDto.getAccesses().get(0).setRole(null);
        taskDto.getAccesses().get(1).setUser(null);
        taskDto.getAccesses().get(2).setUser(null);

        Assertions.assertFalse(taskDto.isCorrectInput());
        Assertions.assertTrue(taskDto.getAccesses().size() == 0);
        Assertions.assertFalse(taskDto.getInfo().equals(""));

        //Проверка при не корректных значениях 2
        taskDto = createCorrectObject();
        taskDto.setAccesses(null);

        Assertions.assertFalse(taskDto.isCorrectInput());
        Assertions.assertFalse(taskDto.getInfo().equals(""));

        //Проверка при не корректных значениях
        taskDto = createCorrectObject();
        taskDto.getTask().setTitle("");

        Assertions.assertFalse(taskDto.isCorrectInput());
        Assertions.assertFalse(taskDto.getInfo().equals(""));

        //Проверка при не корректных значениях 4
        taskDto = createCorrectObject();
        taskDto.getTask().setDescription("");

        Assertions.assertFalse(taskDto.isCorrectInput());
        Assertions.assertFalse(taskDto.getInfo().equals(""));

        //Проверка при не корректных значениях 5
        taskDto = createCorrectObject();
        taskDto.getTask().setStatus(null);

        Assertions.assertFalse(taskDto.isCorrectInput());
        Assertions.assertFalse(taskDto.getInfo().equals(""));

        //Проверка при не корректных значениях 6
        taskDto = createCorrectObject();
        taskDto.setTask(null);

        Assertions.assertFalse(taskDto.isCorrectInput());
        Assertions.assertFalse(taskDto.getInfo().equals(""));

        //Проверка при не корректных значениях 7
        taskDto = createCorrectObject();
        taskDto.setAccesses(null);

        Assertions.assertFalse(taskDto.isCorrectInput());
        Assertions.assertFalse(taskDto.getInfo().equals(""));

        //Проверка при не корректных значениях 8
        taskDto = createCorrectObject();
        sizeAccesses = taskDto.getAccesses().size();

        taskDto.getAccesses().addAll(createCorrectObject().getAccesses());



        Assertions.assertTrue(taskDto.isCorrectInput());
        Assertions.assertTrue(taskDto.getAccesses().size() == sizeAccesses);
        Assertions.assertTrue(taskDto.getInfo().equals(""));





    }

    /**
     * Создание корректного объекта
     */
    public TaskDto createCorrectObject(){
        List<User> userList = userService.listAllUsers();

        List<TaskStatus> taskStatusList = taskService.listAllStatusesTasks();
        List<TaskUserRole> taskUserRoles = taskService.listAllUserRolesToTask();

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

        TaskDto taskDto = new TaskDto();
        taskDto.setTask(task);
        taskDto.setAccesses(listAccesses);

        return taskDto;
    }


}