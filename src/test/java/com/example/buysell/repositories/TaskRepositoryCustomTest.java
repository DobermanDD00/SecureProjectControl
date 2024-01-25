package com.example.buysell.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskRepositoryCustomTest {

    @Autowired
    TaskRepositoryCustom taskRepositoryCustom;

    @Test
    void getNextId() {
        System.out.println(taskRepositoryCustom.getNextId() + "_____________");
    }
}