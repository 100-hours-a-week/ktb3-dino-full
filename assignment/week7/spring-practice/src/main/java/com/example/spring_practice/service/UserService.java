package com.example.spring_practice.service;

import com.example.spring_practice.entity.Comment;
import com.example.spring_practice.entity.Like;
import com.example.spring_practice.entity.User;
import com.example.spring_practice.repository.CommentRepository;
import com.example.spring_practice.repository.LikeRepository;
import com.example.spring_practice.repository.PostRepository;
import com.example.spring_practice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void deleteAccount(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("User not found: " + userId));

        var likes = likeRepository.findByUser_Id(userId);
        for (Like like : likes) {
            postRepository.addLikesCount(like.getPost().getId(), -1);
        }
        likeRepository.deleteAll(likes);

        var comments = commentRepository.findByAuthor_Id(userId);

        Map<Long, Integer> byPost = new HashMap<>();
        for (Comment c : comments) {
            byPost.merge(c.getPost().getId(), 1, Integer::sum);
        }
        for (var e : byPost.entrySet()) {
            postRepository.addCommentsCount(e.getKey(), -e.getValue());
        }
        commentRepository.deleteAll(comments);
        postRepository.deleteByAuthor_Id(userId);
        userRepository.delete(user);

    }

}
