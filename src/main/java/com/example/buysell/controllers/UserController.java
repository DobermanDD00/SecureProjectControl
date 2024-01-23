package com.example.buysell.controllers;

import com.example.buysell.models.Security.Security;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.security.KeyPair;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }


    @SneakyThrows
    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        KeyPair keyPair = Security.generatedRsaKeys();
        if (!userService.createUser(user, keyPair)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getEmail() + " уже существует");
            return "registration";
        }
        //TODO 000 вывод приватного ключа или сохранение в файле
        model.addAttribute("user", user);
        model.addAttribute("keyPublic", new String(keyPair.getPublic().getEncoded()));
        model.addAttribute("keyPrivate", new String(keyPair.getPrivate().getEncoded()));
        return "show-key";
//        return "redirect:/login";
    }

    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model) {
        model.addAttribute("user", user);
//        model.addAttribute("products", user.getProducts());
        return "user-info";
    }
    @GetMapping("/checkPriKey")
    public String checkPriKey(HttpSession httpSession){
        Object privkey = httpSession.getAttribute("privkey");



        return  null;
    }





}
