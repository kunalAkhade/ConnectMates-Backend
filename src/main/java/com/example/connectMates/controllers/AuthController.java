package com.example.connectMates.controllers;

import com.example.connectMates.dto.UserDTO;
import com.example.connectMates.entities.User;
import com.example.connectMates.service.AuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthServices authServices;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@ModelAttribute UserDTO userDTO) throws IOException {
        return new ResponseEntity<>(authServices.registerUser(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> map){
        Object authResult = authServices.loginUser(map);
        if(authResult!="INVALID_CRED"){
            return new ResponseEntity<>(authServices.loginUser(map), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(authServices.loginUser(map), HttpStatus.UNAUTHORIZED);
    }
}
