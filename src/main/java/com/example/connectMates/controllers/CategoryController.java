package com.example.connectMates.controllers;

import com.example.connectMates.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getCategory(@PathVariable Long id){
        return new ResponseEntity<>(service.getCategory(id), HttpStatus.OK);
    }

    @GetMapping("/get/all")
    public ResponseEntity<?> getAllCategories(){
        return new ResponseEntity<>(service.getAllCategory(),HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Map<String,String> request){
        return new ResponseEntity<>(service.addCategory(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(@RequestParam Long id){
        return new ResponseEntity<>(service.deleteCategory(id), HttpStatus.OK);
    }
}



