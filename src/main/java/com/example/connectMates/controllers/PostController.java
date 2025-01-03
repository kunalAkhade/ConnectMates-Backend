package com.example.connectMates.controllers;

import com.example.connectMates.dto.PostDTO;
import com.example.connectMates.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/post")
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@ModelAttribute PostDTO map) throws IOException {
        log.info("in post");
        return new ResponseEntity<>(postService.addPost(map), HttpStatus.OK);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getPost(@PathVariable Long id){
        return new ResponseEntity<>(postService.getPost(id), HttpStatus.OK);
    }

    @GetMapping("get/{id}/all")
    public ResponseEntity<?> getAllPost(@PathVariable Long id){
        return new ResponseEntity<>(postService.getALLPosts(id), HttpStatus.OK);
    }
}
