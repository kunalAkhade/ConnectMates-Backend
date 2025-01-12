package com.example.connectMates.controllers;

import com.example.connectMates.entities.User;
import com.example.connectMates.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id){
        Map<String,Object> user = userService.getUser(id);
        if(user==null){
            return new ResponseEntity<>("User doesn't exist",HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Follow a user
    @PostMapping("/{followingId}/follow/{followerId}")
    public ResponseEntity<?> followUser(@PathVariable Long followingId, @PathVariable Long followerId) {
        return new ResponseEntity<>(userService.followUser(followingId, followerId), HttpStatus.OK);
    }

    // Unfollow a user
    @DeleteMapping("/{followingId}/unfollow/{followerId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followingId, @PathVariable Long followerId) {
       return new ResponseEntity<>(userService.unfollowUser(followingId, followerId), HttpStatus.OK);
    }

    // Get all followers of a user
    @GetMapping("/{userId}/followers")
    public ResponseEntity<Set<User>> getFollowers(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getFollowers(userId), HttpStatus.OK);
    }

    @GetMapping("/getAuthenticatedUser")
    public ResponseEntity<?> getAuthUser(){
        User user = userService.getAuthUser();
        if(user!=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
    }
}
