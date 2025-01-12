package com.example.connectMates.service;

import com.example.connectMates.dao.CategoryDao;
import com.example.connectMates.dao.UserDao;
import com.example.connectMates.dto.UserDTO;
import com.example.connectMates.entities.Category;
import com.example.connectMates.entities.User;
import com.example.connectMates.entities.UserCategory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class AuthServices {

    private static final Logger log = LoggerFactory.getLogger(AuthServices.class);
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CategoryDao categoryDao;



    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtServices service;

    public String registerUser(UserDTO userDTO) throws IOException {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setBio(userDTO.getBio());
        user.setProfilePicture(userDTO.getProfilePicture().getBytes());
        List<Category> list = categoryDao.findAllById(userDTO.getCategories());
        user.setCategories(new HashSet<>(list));



        userDao.save(user);

        return "User Created";

    }

    public Object loginUser(Map<String, String> map) {
        log.info("user is login ");
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(map.get("username"), map.get("password"));
        try {
            Authentication authToken = authenticationManager.authenticate(authRequest);
            if (authToken.isAuthenticated()) {
               return service.generateToken(map.get("username"));
            }
        } catch (Exception e) {
            log.error("Authentication failed: ", e);
        }
        return "INVALID_CRED";
    }
}
