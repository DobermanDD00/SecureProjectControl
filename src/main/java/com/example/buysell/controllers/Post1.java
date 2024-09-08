package com.example.buysell.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Post1 {
    private int id;
    private String title;
    private String body;
    private int userId;

    public String toString() {
        return String.format("\n id: %s \n title: %s \n body: %s \n userId: %s \n", id, title, body, userId);
    }
}
