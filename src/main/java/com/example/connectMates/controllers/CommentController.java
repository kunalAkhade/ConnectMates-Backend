package com.example.connectMates.controllers;

import com.example.connectMates.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody Map<String, String> request){
        return new ResponseEntity<>(commentService.addComment(request), HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getComments(@PathVariable  Long id){
        return new ResponseEntity<>(commentService.getComment(id), HttpStatus.CREATED);
    }








}
