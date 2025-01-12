package com.example.connectMates.controllers;

import com.example.connectMates.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<?> getHomePageContent(){
        return new ResponseEntity<>(postService.getALLPosts(), HttpStatus.OK);
    }



}
