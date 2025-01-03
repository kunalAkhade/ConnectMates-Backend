package com.example.connectMates.dao;

import com.example.connectMates.entities.UserCategory;
import com.example.connectMates.entities.UserCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCategoryDao extends JpaRepository<UserCategory, UserCategoryId> {

    @Query(value = "Select * from user_category where user_id = :userID and category_id = :categoryID ", nativeQuery = true)
    UserCategory findUserCategoryByUserAndCategory(Long userID, Long categoryID);

    @Query(value = "Select * from user_category where user_id = :userID", nativeQuery = true)
    List<UserCategory> findUserCategoryByUser(Long userID);

    @Query(value = "Select comment_score from user_category where category_id = :categoryID ORDER BY comment_score DESC LIMIT 1", nativeQuery = true)
    float findMaxCommentScore(Long categoryID);
}
