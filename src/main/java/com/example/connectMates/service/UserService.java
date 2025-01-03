package com.example.connectMates.service;

import com.example.connectMates.dao.UserCategoryDao;
import com.example.connectMates.dao.UserDao;
import com.example.connectMates.dto.UserDTO;
import com.example.connectMates.entities.Category;
import com.example.connectMates.entities.User;
import com.example.connectMates.entities.UserCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userRepository;

    @Autowired
    private UserCategoryDao userCategoryDao;

    // Add a follower (User A follows User B)
    @Transactional
    public String followUser(Long followingId, Long followerId) {
        Optional<User> followingOptional = userRepository.findById(followingId);
        Optional<User> followerOptional = userRepository.findById(followerId);

        if (followingOptional.isPresent() && followerOptional.isPresent()) {
            User following = followingOptional.get();
            User follower = followerOptional.get();

            following.getFollowers().add(follower);
            follower.getFollowing().add(following); // Update the inverse side
            userRepository.save(following);
            userRepository.save(follower); // Save the updated "following" user
            return "User is followed";
        }
        return "User doesn't exists or error occurred";
    }

    // Remove a follower (User A unfollows User B)
    @Transactional
    public String unfollowUser(Long followingId, Long followerId) {
        Optional<User> followingOptional = userRepository.findById(followingId);
        Optional<User> followerOptional = userRepository.findById(followerId);

        if (followingOptional.isPresent() && followerOptional.isPresent()) {
            User following = followingOptional.get();
            User follower = followerOptional.get();

            following.getFollowers().remove(follower);
            follower.getFollowing().remove(following); // Update the inverse side
            userRepository.save(following);
            userRepository.save(follower); // Save the updated "following" user
            return "Unfollowing user is successful";
        }
        return "User doesn't exist or error occurred";
    }

    // Get all followers of a user
    public Set<User> getFollowers(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.map(User::getFollowers).orElse(null);
    }

    public Map<String, Object> getUser(Long id){
       Optional<User> user = userRepository.findById(id);
       if(user.isPresent()){
           Map<String,Object> map = new HashMap<>();
           map.put("id", user.get().getId());
           map.put("username", user.get().getUsername());
           map.put("profilePicture", Base64.getEncoder().encodeToString(user.get().getProfilePicture()));
           map.put("email",user.get().getEmail());

           List<UserCategory> list = userCategoryDao.findUserCategoryByUser(user.get().getId());
           Set<Map<String,Object>> set = new HashSet<>();
           for(UserCategory category:list){
             Map<String,Object> mp=new HashMap<>();
             mp.put("id",category.getCategory().getId());
             mp.put("category",category.getCategory().getCategoryName());
             mp.put("commentScore",category.getCommentScore());
             mp.put("likesScore", category.getLikeScore());
             set.add(mp);
           }
           map.put("categories",set);
           map.put("followers",user.get().getFollowers().size());
           map.put("following",user.get().getFollowing().size());
           map.put("post",user.get().getPosts().size());


           return map;
       }
       return null;
    }
}
