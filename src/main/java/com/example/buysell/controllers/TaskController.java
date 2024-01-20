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




    @GetMapping("/task/{idTask}")
    public String taskInfo(@PathVariable Long idTask, Model model) {
        model.addAttribute("task", taskService.getTaskById(idTask));
        model.addAttribute("accesses", taskService.getAccessesToTaskById(idTask));

//        System.out.println(taskService.getTaskById(idTask).getId()+" ++++++++++++++++");
//        for (TaskAccess taskAccess : taskService.getAccessesToTaskById(idTask)){
//            System.out.println(taskAccess.getId()+" -----------------------------");
//        }
//        model.addAttribute("images", task.getImages());
        return "task-info";
    }


//    @PostMapping("/task/create")
//    public String createTask(@RequestParam("file1") MultipartFile file1, @ModelAttribute ("task") Task task, Principal principal) throws IOException {
//        taskService.saveTask(principal, task, file1);
//        return "redirect:/";
//    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }
    @GetMapping("/task/create")
    public String newTasks1(Model model) {
        model.addAttribute("users", userService.list());
        model.addAttribute("rolesTask", taskService.listRoles());
        model.addAttribute("statuses", taskService.listStatuses());
        model.addAttribute("task", new Task());

        TaskAccessCreationDto accessesForm = new TaskAccessCreationDto();
        for (int i = 0; i < 5; i++){
            accessesForm.addAccess(new TaskAccess());
        }
        model.addAttribute("accesses", accessesForm.getAccesses());
        model.addAttribute("form", accessesForm);
        return "task-create";
    }
    @GetMapping("/task/edit/{id}")
    public String taskEdit(@PathVariable Long id, Model model, Principal principal){
        // TODO проверки на права доступа
        Task task = taskService.getTaskById(id);

        model.addAttribute("user", taskService.getUserByPrincipal(principal));
        model.addAttribute("users", userService.list());
        model.addAttribute("task", taskService.getTaskById(id));
        model.addAttribute("rolesTask", taskService.listRoles());
        model.addAttribute("statuses", taskService.listStatuses());
        TaskAccessCreationDto accessesForm = new TaskAccessCreationDto();
        accessesForm.setAccesses(taskService.getAccessesToTaskById(id));
        accessesForm.getAccesses().add(new TaskAccess());
        accessesForm.getAccesses().add(new TaskAccess());
        accessesForm.getAccesses().add(new TaskAccess());
        model.addAttribute("accesses", accessesForm.getAccesses());
        model.addAttribute("form", accessesForm);


        return "task-edit";
    }

    @PostMapping("/task/save")
    public String create2(@ModelAttribute Task task, @ModelAttribute TaskAccessCreationDto form, Model model) {
        taskService.saveTaskAndAccesses(task, form.getAccesses());
        return "redirect:/";
    }
    @PostMapping("/task/update/{idTask}")
    public String taskUpdate(@PathVariable Long idTask, @ModelAttribute Task task, @ModelAttribute TaskAccessCreationDto form, Model model){
        task.setId(idTask);//TODO Костыль спросить Олега почему id сбрасывается на 0, а остальное нет, (дата не сбрасывается, хотя в форму она не попадает)
        taskService.updateTaskAndAccesses(task, form.getAccesses());
        return "redirect:/"; //TODO не работает
    }


}
