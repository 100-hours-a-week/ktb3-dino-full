package com.example.spring_practice.repository;

import com.example.spring_practice.entity.Like;
import com.example.spring_practice.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface LikeRepository extends JpaRepository<Like, LikeId> {
    boolean existsByPostIdAndUserId(Long postId, Long userId);
    long countByPostId(Long postId);
    long deleteByPostIdAndUserId(Long postId, Long userId);

    List<Like> findByUser_Id(Long userId);
    int deleteByUser_Id(Long userId);
}