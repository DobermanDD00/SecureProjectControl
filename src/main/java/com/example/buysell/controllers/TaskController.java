package com.example.buysell.controllers;

import com.example.buysell.models.Security.Keys;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.TaskPackage.TaskAccess;
import com.example.buysell.models.TaskPackage.TaskAccessCreationDto;

import com.example.buysell.models.TaskPackage.TaskCreationDto;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.services.TaskService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.security.PrivateKey;
import java.util.Arrays;

@Slf4j
@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;


    @GetMapping("/")
    public String tasks(@RequestParam(name = "title", required = false) String title, Principal principal, Model model, HttpSession httpSession) {
        User userCurrent = taskService.getUserByPrincipal(principal);
        model.addAttribute("user", userCurrent);
        Object keys = httpSession.getAttribute("keysCurrentUser");
        if (keys == null) {
            model.addAttribute("message", "");
            return "inputPrivateKey";
        }
        Keys keysCurrentUser = (Keys) keys;
//        log.info("Public\n{}\n Private\n{}", new String(keysCurrentUser.getPublicKey().getEncoded()), new String(keysCurrentUser.getPrivateKey().getEncoded()));


        model.addAttribute("tasksUser", taskService.tasksToUser(userCurrent, keysCurrentUser.getPrivateKey()));
//        model.addAttribute("tasks", taskService.tasksByTitleOrAll(title));

//        model.addAttribute("users", userService.list());
//        model.addAttribute("statuses", taskService.listStatusesTasks());

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



        model.addAttribute("task", taskService.getTaskById(idTask, taskService.getUserByPrincipal(principal), keysCurrentUser.getPrivateKey()));
        model.addAttribute("accesses", taskService.getAccessesToTaskById(idTask));

//        model.addAttribute("images", task.getImages());
        return "task-info";
    }


//    @PostMapping("/task/create")
//    public String createTask(@RequestParam("file1") MultipartFile file1, @ModelAttribute ("task") Task task, Principal principal) throws IOException {
//        taskService.saveTask(principal, task, file1);
//        return "redirect:/";
//    }


    @GetMapping("/task/create")
    public String newTasks1(Model model) {
        model.addAttribute("listUsersToTask", userService.list());
        model.addAttribute("listUserRolesToTask", taskService.listUserRolesToTask());
        model.addAttribute("listStatusesTasks", taskService.listStatusesTasks());

        TaskCreationDto taskCreationDto = new TaskCreationDto();
        for (int i = 0; i < 5; i++)
            taskCreationDto.addAccess(new TaskAccess());

        model.addAttribute("taskCreationDto", taskCreationDto);

        return "task-create";
    }

    @SneakyThrows
    @PostMapping("/task/save")
    public String create2(@ModelAttribute TaskCreationDto taskCreationDto, Principal principal, Model model) {

        if (!taskCreationDto.isCorrectInput()) {
            model.addAttribute("listUsersToTask", userService.list());
            model.addAttribute("listUserRolesToTask", taskService.listUserRolesToTask());
            model.addAttribute("listStatusesTasks", taskService.listStatusesTasks());

            for (int i = 0; i < 5; i++)
                taskCreationDto.addAccess(new TaskAccess());
            return "task-create";
        }

        User currentUser = taskService.getUserByPrincipal(principal);
//        System.out.println(new String(currentUser.getPubKey()));
        taskService.saveTaskCreationDto(taskCreationDto, currentUser);
        return "redirect:/";

    }

    @GetMapping("/task/edit/{id}")
    public String taskEdit(@PathVariable Long id, Model model, Principal principal, HttpSession httpSession) {
        Object keys = httpSession.getAttribute("keysCurrentUser");
        if (keys == null) {
            model.addAttribute("message", "");
            return "inputPrivateKey";
        }
        Keys keysCurrentUser = (Keys) keys;
        Task task = taskService.getTaskById(id, taskService.getUserByPrincipal(principal), keysCurrentUser.getPrivateKey());

        model.addAttribute("user", taskService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.list());
        model.addAttribute("task", taskService.getTaskById(id, taskService.getUserByPrincipal(principal), keysCurrentUser.getPrivateKey()));
        model.addAttribute("rolesTask", taskService.listUserRolesToTask());
        model.addAttribute("statuses", taskService.listStatusesTasks());

        TaskAccessCreationDto accessesForm = new TaskAccessCreationDto();
        accessesForm.setAccesses(taskService.getAccessesToTaskById(id));
        accessesForm.getAccesses().add(new TaskAccess());
        accessesForm.getAccesses().add(new TaskAccess());
        accessesForm.getAccesses().add(new TaskAccess());
        model.addAttribute("accesses", accessesForm.getAccesses());
        model.addAttribute("form", accessesForm);
        return "task-edit";
    }

    @PostMapping("/task/update/{idTask}")
    public String taskUpdate(@PathVariable Long idTask, Task task, TaskAccessCreationDto form, Model model, Principal principal, HttpSession httpSession) {
        Object keys = httpSession.getAttribute("keysCurrentUser");
        if (keys == null) {
            model.addAttribute("message", "");
            return "inputPrivateKey";
        }
        Keys keysCurrentUser = (Keys) keys;

        task.setId(idTask);
        taskService.updateTaskAndAccesses(task, form.getAccesses(), taskService.getUserByPrincipal(principal), keysCurrentUser.getPrivateKey());

        return "redirect:/";
    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }


    public void filterTaskAccessCreationDto(TaskAccessCreationDto tasks) {
        tasks.getAccesses().removeIf(n -> (n.getUser() == null || n.getRole() == null));
    }


}
