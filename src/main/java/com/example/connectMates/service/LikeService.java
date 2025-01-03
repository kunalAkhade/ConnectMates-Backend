package com.example.connectMates.service;

import com.example.connectMates.dao.PostDao;
import com.example.connectMates.dao.PostLikesDao;
import com.example.connectMates.dao.UserCategoryDao;
import com.example.connectMates.dao.UserDao;
import com.example.connectMates.entities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class LikeService {

    @Autowired
    private PostLikesDao postLikesDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserCategoryDao userCategoryDao;

    public String postLike(Map<String, Long> request) {
        Optional<User> user = userDao.findById(request.get("userID"));
        Optional<Post> post = postDao.findById(request.get("postID"));
        if (user.isPresent() && post.isPresent()) {
            PostLikes postLikes = new PostLikes();
            postLikes.setPostID(post.get());
            postLikes.setUserID(user.get());
            postLikesDao.save(postLikes);

            Long maxLikeCount = postLikesDao.findMaxLikesByCategory(post.get().getCategory().getId());
            System.out.println(maxLikeCount);
            Long likeCount = postLikesDao.findLikesByPostID(post.get().getId());
            System.out.println(likeCount);

            Optional<UserCategory> userCategory = userCategoryDao.findById(new UserCategoryId(user.get().getId(), post.get().getCategory().getId()));
            userCategory.ifPresent(category -> {
                float score = (float) likeCount / maxLikeCount;
                category.setLikeScore(score);
                userCategoryDao.save(category); // Ensure the update is saved
            });
            return "post is liked";
        }
        return "Invalid user or post";

    }

    public List<Map<String, Object>> getLikes(Long id) {
        if (postDao.findById(id).isPresent()) {
            List<Long> users = postLikesDao.findPostLikesByUserID(id);
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            for (Long i : users) {
                Optional<User> user = userDao.findById(i);
                if (user.isPresent()) {
                    map.put("id", user.get().getId());
                    map.put("username", user.get().getUsername());
                    map.put("media", Base64.getEncoder().encodeToString(user.get().getProfilePicture()));
                    list.add(map);
                }
            }
            return list;
        }
        return null;
    }

}
