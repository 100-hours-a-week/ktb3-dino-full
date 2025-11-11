package com.example.spring_practice.repository;


import com.example.spring_practice.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.viewCount = p.viewCount + 1 where p.id = :postId")
    int increaseViews(long postId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.commentsCount = p.commentsCount + :delta where p.id = :postId")
    int addCommentsCount(long postId, int delta);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Post p set p.likesCount = p.likesCount + :delta where p.id = :postId")
    int addLikesCount(long postId, int delta);


    List<Post> findByAuthor_Id(Long userId);
    int deleteByAuthor_Id(Long userId);
}
