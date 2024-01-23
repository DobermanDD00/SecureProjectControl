package com.example.buysell.controllers;

import com.example.buysell.models.TaskPackage.Task;
import com.example.buysell.models.TaskPackage.TaskAccess;
import com.example.buysell.models.TaskPackage.TaskAccessCreationDto;

import com.example.buysell.models.UserPackage.User;
import com.example.buysell.services.TaskService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;


    @GetMapping("/")
    public String tasks(@RequestParam(name = "title", required = false) String title, Principal principal, Model model, HttpSession httpSession) {
        Object privkey = httpSession.getAttribute("privkey");

        User userCurrent = taskService.getUserByPrincipal(principal);
        model.addAttribute("user", userCurrent);

        if (privkey == null) {
            return "privkey";
        }
        model.addAttribute("tasksUser", taskService.tasksToUser(userCurrent, privkey));
//        model.addAttribute("tasks", taskService.tasksByTitleOrAll(title));

//        model.addAttribute("users", userService.list());
//        model.addAttribute("statuses", taskService.listStatusesTasks());

        return "tasks";
    }


    @PostMapping("/key")
    public String postKey(@RequestParam("privkey") String privkey, HttpSession httpSession) {
        httpSession.setAttribute("privkey", privkey);
        return "redirect:/";
    }


    @GetMapping("/task/{idTask}")
    public String taskInfo(@PathVariable Long idTask, Model model, Principal principal, HttpSession httpSession) {
        Object privkey = httpSession.getAttribute("privkey");
        System.out.println(privkey.toString());

        model.addAttribute("task", taskService.getTaskById(idTask, taskService.getUserByPrincipal(principal), privkey));
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

    @SneakyThrows
    @PostMapping("/task/save")
    public String create2(@ModelAttribute Task task, @ModelAttribute TaskAccessCreationDto form, Principal principal) {
        taskService.createTaskAndAccesses(task, form.getAccesses(), taskService.getUserByPrincipal(principal));
        return "redirect:/";
    }

    @GetMapping("/task/edit/{id}")
    public String taskEdit(@PathVariable Long id, Model model, Principal principal, HttpSession httpSession) {
        Object privkey = httpSession.getAttribute("privkey");
        Task task = taskService.getTaskById(id, taskService.getUserByPrincipal(principal), privkey);

        model.addAttribute("user", taskService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.list());
        model.addAttribute("task", taskService.getTaskById(id, taskService.getUserByPrincipal(principal), privkey));
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
        Object privkey = httpSession.getAttribute("privkey");

        task.setId(idTask);
        taskService.updateTaskAndAccesses(task, form.getAccesses(), taskService.getUserByPrincipal(principal), privkey);

        return "redirect:/";
    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }


}
