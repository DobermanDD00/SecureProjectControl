package com.example.buysell.controllers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@FeignClient(name = "jsonplaceholder", url = "https://jsonplaceholder.typicode.com", path = "/posts")

public interface ApiClientTest {
    @GetMapping( value = "/", consumes = APPLICATION_JSON_VALUE)
    List<Post1> getPosts(@RequestParam("_limit") final int postLimit);

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    Post1 savePost(@RequestBody Post1 post);
}
