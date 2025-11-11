package com.example.spring_practice.repository;

import com.example.spring_practice.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_IdOrderByCreatedAtAsc(Long postId);

    @Query("""
        select c from Comment c
        join fetch c.author a
        where c.post.id = :postId
        order by c.createdAt asc, c.id asc
    """)
    List<Comment> findAllByPostIdWithAuthorOrderByCreated(@Param("postId") Long postId);

    List<Comment> findByAuthor_Id(Long userId);
    int deleteByAuthor_Id(Long userId);
}