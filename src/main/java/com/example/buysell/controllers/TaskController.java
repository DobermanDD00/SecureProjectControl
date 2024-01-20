package com.example.buysell.controllers;

import com.example.buysell.models.TaskPackage.TaskAccess;
import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.TaskPackage.TaskAccessCreationDto;
import com.example.buysell.repositories.ChildRepository;
import com.example.buysell.repositories.ParentRepository;
import com.example.buysell.services.TaskService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final ParentRepository parentRepository;
    private final ChildRepository childRepository;

    @GetMapping("/")
    public String tasks(@RequestParam(name = "title", required = false) String title, Principal principal, Model model) {
        model.addAttribute("tasks", taskService.listTasks(title));
        model.addAttribute("user", taskService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.list());
        model.addAttribute("statuses", taskService.listStatuses());

        return "tasks";
    }

    @GetMapping("/task/edit/{id}")
    public String taskEdit(@PathVariable Long id, Model model, Principal principal){
        // TODO проверки на права доступа
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        model.addAttribute("user", taskService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.list());
        model.addAttribute("statuses", taskService.listStatuses());
        return "task-edit";
    }
    @PostMapping("/task/update/{id}")
    public String taskUpdate(@PathVariable Long id, Task task, Model model){
        // TODO проверки на права доступа
        Task taskOld = taskService.getTaskById(id);
        taskService.taskUpdate(task, taskOld);
        return "redirect:/"; //TODO не работает
    }

    @GetMapping("/task/{id}")
    public String taskInfo(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
//        model.addAttribute("images", task.getImages());
        return "task-info";
    }


    @PostMapping("/task/create")
    public String createTask(@RequestParam("file1") MultipartFile file1, @ModelAttribute ("task") Task task, Principal principal) throws IOException {
        taskService.saveTask(principal, task, file1);
        return "redirect:/";
    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }
    @GetMapping("/create-new-task")
    public String newTasks1(Model model) {
        model.addAttribute("users", userService.list());
        model.addAttribute("rolesTask", taskService.listRoles());
        model.addAttribute("statuses", taskService.listStatuses());
        model.addAttribute("task", new Task());



        TaskAccessCreationDto accessesForm = new TaskAccessCreationDto();

        for (int i = 0; i < 5; i++){
            accessesForm.addAccess(new TaskAccess());
//            accessesForm.getAccesses().get(i).setId(i);
        }

        model.addAttribute("accesses", accessesForm.getAccesses());

        model.addAttribute("form", accessesForm);

        return "create-task";
    }

    @PostMapping("/save-task")
    public String create2(@ModelAttribute Task task, @ModelAttribute TaskAccessCreationDto form, Model model) {
        taskService.saveTaskAndAccesses(task, form.getAccesses());
        return "redirect:/";
    }


}
