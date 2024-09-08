package com.example.buysell.services;

import com.example.buysell.controllers.JSONPlaceHolderClient;
import com.example.buysell.controllers.Post1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {
    private final JSONPlaceHolderClient jsonPlaceHolderClient;

    public Post1 test1(Long id){
        System.out.println("12345");
        return jsonPlaceHolderClient.getPostById(id);
    }
}
