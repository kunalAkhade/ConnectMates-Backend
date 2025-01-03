package com.example.connectMates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

@Entity
@Table(name = "user_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCategory  {

    @EmbeddedId
    private UserCategoryId id = new UserCategoryId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;



    @Column(nullable = true, columnDefinition = "FLOAT DEFAULT 0")
    private float likeScore;

    @Column(nullable = true, columnDefinition = "FLOAT DEFAULT 0")
    private float commentScore;





}
