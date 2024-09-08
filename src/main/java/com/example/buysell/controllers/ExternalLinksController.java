package com.example.buysell.controllers;

import com.example.buysell.services.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
//@RequestMapping("/test-json")
@RequiredArgsConstructor
public class ExternalLinksController {
    private final TestService testService;


//    private final UserService userService;

//    @SneakyThrows//**************
    @GetMapping("/test-json")
//    @ResponseBody
    public Post1 test1() {


        return testService.test1(1L);






//        return "redirect:/";
    }



}

