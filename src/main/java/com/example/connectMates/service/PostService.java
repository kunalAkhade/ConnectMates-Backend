package com.example.connectMates.service;

import com.example.connectMates.Utilities.MyUtilities;
import com.example.connectMates.dao.CategoryDao;
import com.example.connectMates.dao.PostDao;
import com.example.connectMates.dao.UserDao;
import com.example.connectMates.dto.PostDTO;
import com.example.connectMates.entities.Category;
import com.example.connectMates.entities.Post;
import com.example.connectMates.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class PostService {
    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    PostDao postDao;

    @Autowired
    private MyUtilities utilities;

    public String addPost(PostDTO map) throws IOException {
        log.info("in post service");
        Post post = new Post();
        post.setCategory(categoryDao.findById(map.getCategory()).get());
        post.setUser(userDao.findById(map.getUser()).get());
        post.setMedia(map.getMedia().getBytes());
        postDao.save(post);
        return "successfully posted";
    }

    public Map<String, Object> getPost(Long id){
        Optional<Post> post = postDao.findById(id);
        Map<String,Object> map = new HashMap<>();
        if (post.isPresent()) {
            map.put("id", post.get().getId());
            map.put("category", post.get().getCategory().getId());
            map.put("media",Base64.getEncoder().encodeToString(post.get().getMedia()));
            map.put("user",post.get().getUser().getId());
            return map;
        } else {
            log.warn("Post with ID {} not found.", id);
        }
       return null;
    }

    public boolean deletePost(Long id){
        UserDetails userDetails =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = userDao.findByUsername(username);
        Optional<Post> post = postDao.findById(id);

        if (post.isPresent()) {
            if(!Objects.equals(post.get().getUser().getId(), user.getId())){
                return false;
            }
            postDao.deleteById(id);
            log.info("Post with ID {} deleted successfully.", id);
            return true;
        } else {
            log.warn("Post with ID {} not found.", id);
            return false;
        }
    }

    public List<Map<String, Object>> getALLPosts(Long id){
        List<Map<String, Object>> list = new ArrayList<>();

        Optional<User> user = userDao.findById(id);
        if(user.isPresent() ){

            UserDetails userDetails =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            User otherUser = userDao.findByUsername(username);
            System.out.println(otherUser.getId());
            if(utilities.checkIfFollow(user.get(),otherUser) || !user.get().isPrivateAccount() || user.get().getId()==otherUser.getId()) {
                List<Post> posts = postDao.findAllByUser(id);
                for(Post post: posts){
                    Map<String, Object> map = getPost(post.getId());
                    if(map!=null){
                        list.add(map);
                    }
                }
                return list;
            }
        }
        return list;

    }






    public List<Map<String, Object>> getALLPosts(){
        List<Map<String, Object>> list = new ArrayList<>();

        List<Post> posts = postDao.findAll();
        for(Post post: posts){
            Map<String, Object> map = getPost(post.getId());
            if(map!=null && !post.getUser().isPrivateAccount()){
                list.add(map);
            }
        }
        return list;
    }

}
