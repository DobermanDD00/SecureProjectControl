package com.example.buysell.controllers;

import com.example.buysell.models.Task;
import com.example.buysell.services.TaskService;
import com.example.buysell.services.TaskStatusService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final TaskStatusService taskStatusService;

    @GetMapping("/")
    public String tasks(@RequestParam(name = "title", required = false) String title, Principal principal, Model model) {
        model.addAttribute("tasks", taskService.listTasks(title));
        model.addAttribute("user", taskService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.list());
        model.addAttribute("statuses", taskService.listStatuses());

        return "tasks";
    }

    @GetMapping("/task/{id}")
    public String taskInfo(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        model.addAttribute("images", task.getImages());
        return "task-info";
    }

    @PostMapping("/task/create")
    public String createTask(@RequestParam("file1") MultipartFile file1, Task task, Principal principal) throws IOException {
        taskService.saveTask(principal, task, file1);
        return "redirect:/";
    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }
    @GetMapping("/initialize")
    public String taskInfo() {
        taskStatusService.initialize();
        return "redirect:/";
    }
}
