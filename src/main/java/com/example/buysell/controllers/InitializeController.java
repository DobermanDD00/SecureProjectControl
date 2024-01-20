package com.example.buysell.controllers;

import com.example.buysell.services.TaskService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InitializeController {
    private final TaskService taskService;
    private final UserService userService;



    @GetMapping("/initialize")
    public String taskInfo() {
        userService.initialize();
        taskService.initialize();
        return "redirect:/";
    }
}
