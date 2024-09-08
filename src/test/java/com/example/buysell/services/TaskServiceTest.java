package com.example.buysell.services;

import com.example.buysell.models.Security.Security;
import com.example.buysell.models.TaskPackage.*;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.repositories.FileFunctions;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class TaskServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @SneakyThrows
    @Test
    void getTaskDTOById() {
        User user = userService.findById(2);
        PrivateKey privateKey = Security.decodedKeyPrivateRsa(FileFunctions.readFile(user.getName() + ".txt"));
        System.out.println();
        TaskDto taskDto;
        for (int i = 0; i < 10; i++) {
            taskDto = taskService.getTaskDTOById(1, user, privateKey);
            System.out.println(taskDto.getTask().getDescription());
        }

    }

    @SneakyThrows
    @Test
    void TestTimeAndSpaceCreateTaskDto() {
//        Task task = new Task();
//        task.setTitle("123");
//        byte[] bytes = new byte[200];
//        Arrays.fill(bytes, (byte) 120);
//        task.setDescription(new String(bytes));
//        task.setStatus(taskService.listAllStatusesTasks().get(4));


        TaskDto taskDto  = createCorrectObject();

        for (int i = 0; i < 1000; i++)
            taskService.createTaskDto(taskDto, taskDto.getAccesses().get(1).getUser());

    }

    @SneakyThrows
    @Test
    void TestTimeAndSpaceGetTaskDto() {

        User user = userService.findById(2);
        PrivateKey privateKey = Security.decodedKeyPrivateRsa(FileFunctions.readFile(user.getName() + ".txt"));

        TaskDto taskDto;
        for (int i = 0; i < 1000; i++) {
            taskDto = taskService.getTaskDTOById(1, user, privateKey);
            System.out.println(taskDto.getTask().getDescription());
        }
    }
    @SneakyThrows
    @Test
    void TestTimeAndSpaceUpdateTaskDto() {
        System.out.println(userService.findById(2).getName());
//        User user = userService.findById(2);
//        PrivateKey privateKey = Security.decodedKeyPrivateRsa(FileFunctions.readFile(user.getName() + ".txt"));
//        TaskDto taskDto  = taskService.getTaskDTOById(1, user, privateKey);
//
//
//        for (int i = 0; i < 1000; i++)
//            taskService.updateTaskDto(taskDto, user, privateKey);
    }

    /**
     * Создание корректного объекта
     */
    public TaskDto createCorrectObject() {
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
        access.setUser(userService.findById(2));
        listAccesses.add(access);



        TaskDto taskDto = new TaskDto();
        taskDto.setTask(task);
        taskDto.setAccesses(listAccesses);

        return taskDto;
    }


}