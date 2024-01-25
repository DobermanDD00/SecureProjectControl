package com.example.buysell.services;

import com.example.buysell.models.Security.Security;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.models.enums.Role;
import com.example.buysell.repositories.FileFunctions;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }
    public User getUserById(long idUser){

        return userRepository.findById(idUser);
    }


    @SneakyThrows
    public boolean createUser(User user, KeyPair keyPair) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPubKey(keyPair.getPublic());
        userRepository.save(user);
        log.info("Saving new User with email: {}", email);
        return true;
    }

    public List<User> list() {
        return userRepository.findAll();
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (user.isActive()) {
                user.setActive(false);
                log.info("Ban user with id = {}; email: {}", user.getId(), user.getEmail());
            } else {
                user.setActive(true);
                log.info("Unban user with id = {}; email: {}", user.getId(), user.getEmail());
            }
        }
        userRepository.save(user);
    }

    public void changeUserRoles(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }
    @SneakyThrows
    public void initialize(){
        KeyPair keyPair = Security.generatedRsaKeys();
        User user = new User();
        user.setName("Михаил");
        user.setEmail("Mih@gmail.com");
        user.setPhoneNumber("1");
        user.setPassword("Mih@gmail.com");
        createUser(user, keyPair);
        FileFunctions.writeFile(keyPair.getPrivate().getEncoded(), user.getName()+".txt");


        keyPair = Security.generatedRsaKeys();
        user = new User();
        user.setName("Николай");
        user.setEmail("Nik@gmail.com");
        user.setPhoneNumber("2");
        user.setPassword("Nik@gmail.com");
        createUser(user, keyPair);
        log.info("INITIALIZE USERS");
        FileFunctions.writeFile(keyPair.getPrivate().getEncoded(), user.getName()+".txt");


//        PrivateKey privateKey = Security.decodedKeyPrivateRsa(FileFunctions.readFile(user.getName()+".txt"));
//        PublicKey publicKey1 = keyPair.getPublic();
//        PublicKey publicKey2 = Security.getPublicKeyByPrivateKey(privateKey);
//        System.out.println("\n\n\n\n\n\n\n");
//        System.out.println(publicKey1.equals(publicKey2));
//        System.out.println("\n\n\n\n\n\n\n");




    }
}
