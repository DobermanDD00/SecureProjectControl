package com.example.buysell.controllers;

import com.example.buysell.services.TaskStatusService;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InitializeController {
    private final TaskStatusService taskStatusService;
    private final UserService userService;



    @GetMapping("/initialize")
    public String taskInfo() {
        userService.initialize();
        taskStatusService.initialize();
        return "redirect:/";
    }
}
