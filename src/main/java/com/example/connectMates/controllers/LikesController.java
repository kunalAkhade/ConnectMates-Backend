package com.example.connectMates.controllers;

import com.example.connectMates.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/likes")
public class LikesController {

    @Autowired
    private LikeService service;

    @PostMapping("/post/like")
    public ResponseEntity<?> doLike(@RequestBody Map<String, Long> request){
        return new ResponseEntity<>(service.postLike(request), HttpStatus.CREATED);
    }

    @GetMapping("/get/likes/{id}")
    public ResponseEntity<?> getLikes(@PathVariable Long id ){
        return new ResponseEntity<>(service.getLikes(id),HttpStatus.OK);
    }

}
