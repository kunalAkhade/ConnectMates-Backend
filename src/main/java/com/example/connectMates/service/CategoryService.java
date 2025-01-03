package com.example.connectMates.service;

import com.example.connectMates.dao.CategoryDao;
import com.example.connectMates.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    public Object getCategory(Long id){
        Optional<Category> optionalCategory = categoryDao.findById(id);
        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            return category;
        }
        else{
            return "Category not found";
        }
    }

    public String addCategory(Map<String, String> map) {
        Category category = new Category();
        category.setCategoryName(map.get("name"));
        category.setDescription(map.get("description"));
        categoryDao.save(category);
        return "category added";
    }

    public String deleteCategory(Long id){
        Optional<Category> category = categoryDao.findById(id);
        if(category.isPresent()){
            categoryDao.deleteById(id);
            return "Category deleted";
        }else{
            throw new UsernameNotFoundException("User not found");
        }
    }

    public List<Category> getAllCategory(){
        return categoryDao.findAll();
    }



}
