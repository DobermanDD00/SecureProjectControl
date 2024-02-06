package com.example.buysell.controllers;

import com.example.buysell.models.Security.Keys;
import com.example.buysell.models.Security.Security;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.models.UserPackage.UserDto;
import com.example.buysell.repositories.FileFunctions;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

@Slf4j
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
            log.info("Регистрация нового пользователя провалена, пользователь с email: {}  уже существует" + user.getEmail());
            return "registration";
        }
        // TODO 000 вывод приватного ключа или сохранение в файле
        model.addAttribute("user", user);
        model.addAttribute("keyPublic", new String(keyPair.getPublic().getEncoded()));
        model.addAttribute("keyPrivate", new String(keyPair.getPrivate().getEncoded()));


//        return "show-key";
        return "redirect:/login";
    }

    @SneakyThrows
    @PostMapping("/registration2")
    public String registration1(@ModelAttribute UserDto userDto, HttpServletResponse response, HttpSession httpSession, Model model, Principal principal) {
        KeyPair keyPair = Security.generatedRsaKeys();
        if (!userService.createUser(userDto.getUser(), keyPair)) {
            model.addAttribute("userDto", userDto);
            log.warn("Регистрация нового пользователя провалена, пользователь с email: {}  уже существует" + userDto.getUser().getEmail());
            return "user-create";
        }

        userDto.setPublicKey(keyPair.getPublic());
        userDto.setPrivateKey(keyPair.getPrivate());
        model.addAttribute("userDto", userDto);
        model.addAttribute("PrivateKey", new String(userDto.getPrivateKey().getEncoded()));
        model.addAttribute("PublicKey", new String(userDto.getPublicKey().getEncoded()));
        return "show-key";

//        // TODO 000 вывод приватного ключа или сохранение в файле
//        model.addAttribute("user", user);
//        model.addAttribute("keyPublic", new String(keyPair.getPublic().getEncoded()));
//        model.addAttribute("keyPrivate", new String(keyPair.getPrivate().getEncoded()));
//
//
////        return "show-key";
//        return "user-create";
////        return "redirect:/login";
    }

    @GetMapping("/registration1")
    public String registration1(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user-create";
    }




    @GetMapping("/downloadPrivateKey")
    public void downloadPrivateKey(HttpServletResponse response, HttpSession httpSession, Model model, Principal principal) {
        User userCurrent = userService.getUserByPrincipal(principal);

        Object keys = httpSession.getAttribute("keysCurrentUser");
        if (keys == null) {
            model.addAttribute("message", "");
            return ;
        }
        Keys keysCurrentUser = (Keys) keys;

        byte[] buffer = keysCurrentUser.getPrivateKey().getEncoded();
        String fileName = "PrivateKey"+userCurrent.getEmail() + ".txt";

        response.setContentLengthLong(buffer.length);
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        response.addHeader("Content-Transfer-Encoding", "binary");
        response.addHeader("Expires", "0");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Pragma", "no-cache");

        try {
            OutputStream os = response.getOutputStream();
            os.write(buffer, 0, buffer.length);
            os.flush();
            log.info("Загрузка файла приватного ключа успешно завешена");
            return ;
        } catch (IOException e) {
            log.warn("Загрузка файла приватного ключа провалена, проблемы с потоками");
            e.printStackTrace();
            return ;
        }
    }

    @PostMapping("/downloadPrivateKey2")
    public void downloadPrivateKey2(@ModelAttribute UserDto userDto, HttpServletResponse response) {
        if (!userDto.isCorrect()) {
            log.info("Загрузка файла приватного ключа провалена, проблемы с value userDto");
            return;
        }

        User userCurrent = userDto.getUser();

        byte[] buffer = userDto.getPrivateKey().getEncoded();
        String fileName = "PrivateKey" + userCurrent.getEmail() + ".txt";

        response.setContentLengthLong(buffer.length);
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
        response.addHeader("Content-Transfer-Encoding", "binary");
        response.addHeader("Expires", "0");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Pragma", "no-cache");

        try {
            OutputStream os = response.getOutputStream();
            os.write(buffer, 0, buffer.length);
            os.flush();
            log.info("Загрузка файла приватного ключа успешно завешена");
            return;
        } catch (IOException e) {
            log.warn("Загрузка файла приватного ключа провалена, проблемы с потоками");
            e.printStackTrace();
            return;
        }
    }


    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model) {
        model.addAttribute("user", user);
        return "user-info";
    }

    @PostMapping("/uploadFileKey")
    public String uploadFIleKey(@RequestParam("file") MultipartFile file, Model model, Principal principal, HttpSession httpSession) {

        if (file.isEmpty()) {
            log.warn("Загруженный файл с приватным ключом пустой");
            model.addAttribute("message", "Загруженный файл с приватным ключом пустой");
            return "inputPrivateKey";
        }
        User currentUser = userService.getUserByPrincipal(principal);
        PrivateKey inputPrivateKey;
        try {
            inputPrivateKey = Security.decodedKeyPrivateRsa(file.getBytes());
            log.info("Ввод приватного ключа из загруженного файла");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.warn("Введенное значение из загруженного файла не является приватным ключом");
            inputPrivateKey = null;
        } catch (IOException e) {
            log.warn("Ошибка чтения из загруженного файла приватного ключа");
            inputPrivateKey = null;
        }

        PublicKey curUsrPubKey = currentUser.getPubKey();
        PublicKey inputPrivPubKey = Security.getPublicKeyByPrivateKey(inputPrivateKey);

        if (inputPrivateKey == null || !(curUsrPubKey.equals(inputPrivPubKey))) {
            model.addAttribute("user", currentUser);
            model.addAttribute("message", "Некорректный приватный ключ");
            return "inputPrivateKey";
        }
        log.info("Залогинен пользователь {} с приватным ключем", currentUser.getEmail());
        Keys keysCurrentUser = new Keys(inputPrivateKey, Security.getPublicKeyByPrivateKey(inputPrivateKey));
        httpSession.setAttribute("keysCurrentUser", keysCurrentUser);

        return "redirect:/";
    }


    @PostMapping("/key")
    public String postKey(@RequestParam("privkey") String privkey, Principal principal, Model model, HttpSession httpSession) {
        User currentUser = userService.getUserByPrincipal(principal);
        PrivateKey inputPrivateKey;

        try {
            inputPrivateKey = Security.decodedKeyPrivateRsa(privkey.getBytes());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.warn("Введенное значение не является приватным ключом");
            inputPrivateKey = null;
        }
        try {// TODO удалить потом
            inputPrivateKey = Security.decodedKeyPrivateRsa(FileFunctions.readFile(currentUser.getName() + ".txt"));
            log.info("Ввод приватного ключа из файла");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.warn("Введенное значение из файла не является приватным ключом");
            inputPrivateKey = null;
        }

        PublicKey curUsrPubKey = currentUser.getPubKey();
        PublicKey inputPrivPubKey = Security.getPublicKeyByPrivateKey(inputPrivateKey);

        if (inputPrivateKey == null || !(curUsrPubKey.equals(inputPrivPubKey))) {
            model.addAttribute("user", currentUser);
            model.addAttribute("message", "Некорректный приватный ключ");
            return "inputPrivateKey";
        }
        log.info("Залогинен пользователь {} с приватным ключем", currentUser.getEmail());
        Keys keysCurrentUser = new Keys(inputPrivateKey, Security.getPublicKeyByPrivateKey(inputPrivateKey));
        httpSession.setAttribute("keysCurrentUser", keysCurrentUser);

        return "redirect:/";
    }


}
