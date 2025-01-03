package com.example.connectMates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.Name;

import java.io.PrintStream;

@Entity
@Table(name = "post_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostLikes {

     @EmbeddedId
     private PostLikesID postLikesID = new PostLikesID();

     @ManyToOne
     @MapsId("userID")
     @JoinColumn(name = "user_id")
     private User userID;

     @ManyToOne
     @MapsId("postID")
     @JoinColumn(name = "post_id")
     private Post postID;
}
