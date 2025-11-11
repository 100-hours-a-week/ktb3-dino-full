package com.example.spring_practice.service;


import com.example.spring_practice.entity.Like;
import com.example.spring_practice.entity.Post;
import com.example.spring_practice.entity.User;
import com.example.spring_practice.repository.LikeRepository;
import com.example.spring_practice.repository.PostRepository;
import com.example.spring_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleLike(Long postId, Long userId) {

        Post post = postRepository.findById(postId).orElseThrow(()
                -> new NotFoundException("Post not found: " + postId));
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found: " + userId));

        if (likeRepository.existsByPostIdAndUserId(postId, userId)) {
            long deleted = likeRepository.deleteByPostIdAndUserId(postId, userId);
            if (deleted > 0) postRepository.addLikesCount(postId, -1);
            return false;
        }

        try {
            likeRepository.save(Like.of(post, user));
            postRepository.addLikesCount(postId, 1);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }
}
