package com.example.connectMates.Utilities;

import com.example.connectMates.entities.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MyUtilities {

    public boolean checkIfFollow(User user, User otherUser) {
        if(!user.isPrivateAccount()){
            return true;
        }
        Set<User> followers = user.getFollowers();
        System.out.println(followers.size());
        for(User i : followers){
            System.out.println(i.getId());
            if(i.getId()==otherUser.getId()){
                return true;
            }
        }
        return false;
    }
}
