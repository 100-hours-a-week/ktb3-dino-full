package com.example.spring_practice.service;


import com.example.spring_practice.entity.Comment;
import com.example.spring_practice.entity.Post;
import com.example.spring_practice.entity.User;
import com.example.spring_practice.repository.CommentRepository;
import com.example.spring_practice.repository.PostRepository;
import com.example.spring_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Comment addComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));

        Comment c = Comment.builder()
                .content(content)
                .authorName(user.getNickname())
                .build();

        c.setAuthor(user);
        c.setPost(post);
        postRepository.addCommentsCount(postId, +1);

        return commentRepository.save(c);
    }

    @Transactional
    public void removeComment(Long commentId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found: " + commentId));
        Long postId = c.getPost().getId();
        commentRepository.delete(c);
        postRepository.addCommentsCount(postId, -1);
    }
}
