package com.example.buysell.controllers;

import com.example.buysell.models.Security.Keys;
import com.example.buysell.models.Security.Security;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.repositories.FileFunctions;
import com.example.buysell.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
            return "registration";
        }
        // TODO 000 вывод приватного ключа или сохранение в файле
        model.addAttribute("user", user);
        model.addAttribute("keyPublic", new String(keyPair.getPublic().getEncoded()));
        model.addAttribute("keyPrivate", new String(keyPair.getPrivate().getEncoded()));
        return "show-key";
//        return "redirect:/login";
    }

    @GetMapping("/downloadPrivateKey")
    public String downloadPrivateKey(HttpServletResponse response){
        String  fileName="PrivateKey.txt";
        FileFunctions.writeFile("12345".getBytes(), fileName);//Создать хоть какой-то файл
        if(fileName !=null){
            //Set file path
            File file = new File( fileName);
            if(file.exists()){
                response.setContentType("application/force-download");
                //Set mandatory download not to open
                response.addHeader("Content-Disposiution","attachment;fileName="+fileName);
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis=new FileInputStream(file);
                    bis =new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i !=i){
                        os.write(buffer,0,i);
                        i=bis.read(buffer);
                    }
                    log.info("Загрузка файла приватного ключа успешно завешена");
                    return "redirect:/";
//                    return "download successful";
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(bis !=null){
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fis !=null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        log.warn("Загрузка файла приватного ключа провалена");
        return "redirect:/";
//        return "download failed";
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
        try {
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
