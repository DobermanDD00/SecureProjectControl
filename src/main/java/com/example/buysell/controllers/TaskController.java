package com.example.buysell.controllers;

import com.example.buysell.models.Exception.InputException;
import com.example.buysell.models.Security.Keys;
import com.example.buysell.models.TaskPackage.TaskAccess;
import com.example.buysell.models.TaskPackage.TaskDto;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.services.TaskService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;


    @GetMapping("/")
    public String tasks(@RequestParam(name = "title", required = false) String title, Principal principal, Model model, HttpSession httpSession) {
        User userCurrent = userService.getUserByPrincipal(principal);
        model.addAttribute("user", userCurrent);
        Object keys = httpSession.getAttribute("keysCurrentUser");
        if (keys == null) {
            model.addAttribute("message", "");
            return "inputPrivateKey";
        }
        Keys keysCurrentUser = (Keys) keys;

        model.addAttribute("listTasksToUser", taskService.listTasksToUser(userCurrent, keysCurrentUser.getPrivateKey()));

        return "tasks";
    }





    @GetMapping("/task/{idTask}")
    public String taskInfo(@PathVariable Long idTask, Model model, Principal principal, HttpSession httpSession) {
        Object keys = httpSession.getAttribute("keysCurrentUser");
        if (keys == null) {
            model.addAttribute("message", "");
            return "inputPrivateKey";
        }
        Keys keysCurrentUser = (Keys) keys;
        User currentUser = userService.getUserByPrincipal(principal);

        TaskDto taskDto = taskService.getTaskDTOById(idTask, currentUser, keysCurrentUser.getPrivateKey());
        model.addAttribute("taskDto", taskDto);

        return "task-info";
    }



    @GetMapping("/task/create")
    public String newTasks1(Model model) {
        model.addAttribute("listUsersToTask", userService.listAllUsers());
        model.addAttribute("listUserRolesToTask", taskService.listAllUserRolesToTask());
        model.addAttribute("listStatusesTasks", taskService.listAllStatusesTasks());

        TaskDto taskDto = new TaskDto();
        for (int i = 0; i < 5; i++)
            taskDto.addAccess(new TaskAccess());

        model.addAttribute("taskDto", taskDto);

        return "task-create";
    }

    @PostMapping("/task/save")
    public String create2(@ModelAttribute TaskDto taskDto, Principal principal, Model model) {

        if (!taskDto.isCorrectInput()) {
            model.addAttribute("listUsersToTask", userService.listAllUsers());
            model.addAttribute("listUserRolesToTask", taskService.listAllUserRolesToTask());
            model.addAttribute("listStatusesTasks", taskService.listAllStatusesTasks());

            for (int i = 0; i < 5; i++)
                taskDto.addAccess(new TaskAccess());
            return "task-create";
        }

        User currentUser = userService.getUserByPrincipal(principal);
        try {
            taskService.createTaskDto(taskDto, currentUser);
        } catch (InputException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/";

    }

    @GetMapping("/task/edit/{id}")
    public String taskEdit(@PathVariable Long id, Model model, Principal principal, HttpSession httpSession) {
        Object keys = httpSession.getAttribute("keysCurrentUser");
        if (keys == null) {
            model.addAttribute("message", "");
            return "inputPrivateKey";
        }

        User currentUser = userService.getUserByPrincipal(principal);
        Keys keysCurrentUser = (Keys) keys;

        TaskDto taskDto = taskService.getTaskDTOById(id, currentUser, keysCurrentUser.getPrivateKey());
        taskDto.getAccesses().add(new TaskAccess());
        taskDto.getAccesses().add(new TaskAccess());
        taskDto.getAccesses().add(new TaskAccess());

        model.addAttribute("taskDto", taskDto);
        model.addAttribute("listAllUsers", userService.listAllUsers());
        model.addAttribute("listAllUserRolesToTask", taskService.listAllUserRolesToTask());
        model.addAttribute("listAllStatusesTask", taskService.listAllStatusesTasks());

        return "task-edit";
    }

    @PostMapping("/task/update/{idTask}")
    public String taskUpdate(@PathVariable Long idTask, TaskDto taskDto, Model model, Principal principal, HttpSession httpSession) {
        Object keys = httpSession.getAttribute("keysCurrentUser");
        if (keys == null) {
            model.addAttribute("message", "");
            return "inputPrivateKey";
        }
        User currentUser = userService.getUserByPrincipal(principal);
        Keys keysCurrentUser = (Keys) keys;
        taskDto.getTask().setId(idTask);
        try {
            taskService.updateTaskDto(taskDto, currentUser, keysCurrentUser.getPrivateKey());
        } catch (InputException e) {
            throw new RuntimeException(e);
        }


        return "redirect:/";
    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }



}
