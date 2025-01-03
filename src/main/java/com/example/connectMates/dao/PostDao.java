package com.example.connectMates.dao;

import com.example.connectMates.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {

    @Query(value = "Select * from posts where user_id = :id", nativeQuery = true)
    List<Post> findAllByUser(Long id);
}
