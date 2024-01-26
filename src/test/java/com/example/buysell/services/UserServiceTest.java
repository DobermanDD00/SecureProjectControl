package com.example.buysell.services;

import com.example.buysell.models.Security.Security;
import com.example.buysell.models.UserPackage.User;
import com.example.buysell.repositories.FileFunctions;
import com.example.buysell.repositories.TaskDb;
import com.example.buysell.repositories.TaskDbRepository;
import com.example.buysell.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@SpringBootTest
@RequiredArgsConstructor
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskDbRepository taskDbRepository;
    @SneakyThrows
    @Test
    void initialize() {
        userService.initialize();
        userService.initialize();
        userService.initialize();

        PrivateKey privateKey = Security.decodedKeyPrivateRsa(FileFunctions.readFile("Михаил.txt"));
        User user = userRepository.findByName("Михаил");
        PublicKey publicKey = user.getPubKey();

        PublicKey pubByPri = Security.getPublicKeyByPrivateKey(privateKey);
        Assertions.assertEquals(publicKey, pubByPri);




    }

    @SneakyThrows
    @Test
    void createUser() {
        KeyPair keyPair = Security.generatedRsaKeys();
        User user = new User();
        user.setName("Александр");
        user.setEmail("Alex@gmail.com"+Math.random());
        user.setPhoneNumber("1");
        user.setPassword("Alex@gmail.com");
        userRepository.save(user);
//        userService.createUser(user, keyPair);
        FileFunctions.writeFile(keyPair.getPrivate().getEncoded(), user.getName()+".txt");


        User userGet = userService.findById(user.getId());


        user.setName("1111");
        userRepository.save(user);
        userGet.setId(68);
        userGet.setName("2222");
        userRepository.save(userGet);
        PublicKey  publicKeyFromFile = Security.getPublicKeyByPrivateKey(Security.decodedKeyPrivateRsa(FileFunctions.readFile(user.getName()+".txt")));

//        Assertions.assertTrue(userGet.equals(user));
        Assertions.assertEquals(userGet.getPubKey(), keyPair.getPublic());

        Assertions.assertEquals(publicKeyFromFile, keyPair.getPublic());


    }

    @SneakyThrows
    @Test
    void DbCheckPrivate(){

        TaskDb taskDb = new TaskDb();

        taskDb.setId(40);
        taskDbRepository.save(taskDb);
        TaskDb taskDb1 = taskDbRepository.findById(40L).orElse(null);




        User user = new User();
        PublicKey publicKey = Security.generatedRsaKeys().getPublic();
//        user.setId(15L);
        user.setPubKey(publicKey);

        userRepository.save(user);
        User userDb = userRepository.findById(user.getId());
        System.out.println();
        System.out.println(new String(Security.encodedAnyKey(user.getPubKey())));
        System.out.println();
        System.out.println(new String(Security.encodedAnyKey(userDb.getPubKey())));
        Assertions.assertArrayEquals(Security.encodedAnyKey(user.getPubKey()), Security.encodedAnyKey(userDb.getPubKey()));


    }

}