package com.example.buysell.controllers;

import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.TaskPackage.TaskAccess;
import com.example.buysell.models.TaskPackage.TaskAccessCreationDto;

import com.example.buysell.services.TaskService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;


    @GetMapping("/")
    public String tasks(@RequestParam(name = "title", required = false) String title, Principal principal, Model model) {
        model.addAttribute("tasks", taskService.tasksByTitleOrAll(title));
        model.addAttribute("user", taskService.getUserByPrincipal(principal));
//        model.addAttribute("users", userService.list());
//        model.addAttribute("statuses", taskService.listStatusesTasks());

        return "tasks";
    }


    @GetMapping("/task/{idTask}")
    public String taskInfo(@PathVariable Long idTask, Model model) {
        model.addAttribute("task", taskService.getTaskById(idTask));
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
        model.addAttribute("users", userService.list());
        model.addAttribute("rolesTask", taskService.listUserRolesToTask());
        model.addAttribute("statuses", taskService.listStatusesTasks());
        model.addAttribute("task", new Task());

        TaskAccessCreationDto accessesForm = new TaskAccessCreationDto();
        for (int i = 0; i < 5; i++) {
            accessesForm.addAccess(new TaskAccess());
        }
        model.addAttribute("accesses", accessesForm.getAccesses());
        model.addAttribute("form", accessesForm);
        return "task-create";
    }
    @PostMapping("/task/save")
    public String create2(@ModelAttribute Task task, @ModelAttribute TaskAccessCreationDto form, Model model) {
        taskService.saveTaskAndAccesses(task, form.getAccesses());
        return "redirect:/";
    }

    @GetMapping("/task/edit/{id}")
    public String taskEdit(@PathVariable Long id, Model model, Principal principal) {
        Task task = taskService.getTaskById(id);

        model.addAttribute("user", taskService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.list());
        model.addAttribute("task", taskService.getTaskById(id));
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
    public String taskUpdate(@PathVariable Long idTask, Task task, TaskAccessCreationDto form, Model model) {//TODO Совет Олег @ModelAttribute нужно ли использовать
        task.setId(idTask);//TODO Совет Олег Костыль почему id сбрасывается на 0, а остальное нет, (дата не сбрасывается, хотя в форму она не попадает)
        taskService.updateTaskAndAccesses(task, form.getAccesses());

        return "redirect:/";
    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }




}
