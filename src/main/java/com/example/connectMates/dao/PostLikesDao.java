package com.example.connectMates.dao;

import com.example.connectMates.entities.PostLikes;
import com.example.connectMates.entities.PostLikesID;
import com.example.connectMates.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikesDao extends JpaRepository<PostLikes, PostLikesID> {

    @Query(value = "Select user_id from post_likes where post_id=:id", nativeQuery = true)
    List<Long> findPostLikesByUserID(Long id);

    @Query(value = "Select count(*) from post_likes where post_id = :id ", nativeQuery = true)
    Long findLikesByPostID(Long id);

   @Query(value = "Select max(count_column) from (Select count(user_id) as count_column from post_likes where post_id IN (Select id from posts where category_id = :categoryID) group by post_id) as derived_table", nativeQuery = true)
//@Query(value = "SELECT MAX(count_column) FROM ( " +
//        "SELECT COUNT(user_id) AS count_column " +
//        "FROM post_likes " +
//        "WHERE post_id IN (SELECT id FROM posts WHERE category_id = :categoryID) " +
//        "GROUP BY post_id) AS derived_table",
      //  nativeQuery = true)
    Long findMaxLikesByCategory(Long categoryID);

}
