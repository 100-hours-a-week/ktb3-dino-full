package com.example.spring_practice.service;


import com.example.spring_practice.dto.CommentDto;
import com.example.spring_practice.dto.PostDto;
import com.example.spring_practice.entity.Comment;
import com.example.spring_practice.entity.Post;
import com.example.spring_practice.entity.User;
import com.example.spring_practice.repository.CommentRepository;
import com.example.spring_practice.repository.PostRepository;
import com.example.spring_practice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public Post createPost(Long authorId, String title, String content, String imageUrl) {
        User author = userRepository.findById(authorId).orElseThrow(() ->
                new NotFoundException("User not found: " + authorId));

        Post post = Post.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();

        post.setAuthor(author); // authorName까지 동기화
        return postRepository.save(post);
    }

    @Transactional
    public Post getAndIncreaseViews(Long postId) {
        postRepository.increaseViews(postId);

        return postRepository.findById(postId).orElseThrow(()
                -> new NotFoundException("Post not found: " + postId));
    }

    @Transactional
    public PostDto.Detail getDetail(Long postId) {

        postRepository.increaseViews(postId);
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new NotFoundException("게시물이 없습니다. id=" + postId));
        List<Comment> comments = commentRepository.findAllByPostIdWithAuthorOrderByCreated(postId);
        List<CommentDto.Simple> commentDtos = comments.stream()
                .map(c -> CommentDto.Simple.builder()
                        // .id(c.getId())
                        // .authorId(c.getAuthor().getId())
                        .authorNickname(c.getAuthor().getNickname())
                        .content(c.getContent())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();

        return PostDto.Detail.builder()
                // .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                // .authorId(post.getAuthor().getId())
                .authorNickname(post.getAuthor().getNickname())
                .likeCount(post.getLikesCount())
                .commentCount(post.getCommentsCount())
                .views(post.getViewCount() + 1)
                .createdAt(post.getCreatedAt())
                .comments(commentDtos)
                .build();
    }


    @Transactional
    public void deletePost(Long postId, Long requesterId) throws AccessDeniedException {
        Post post = postRepository.findById(postId).orElseThrow(()
                -> new NotFoundException("Post not found: " + postId));

        Long authorId = post.getAuthor().getId();
        if (!authorId.equals(requesterId)) {
            throw new AccessDeniedException("not the author");
        }

        postRepository.delete(post);
    }

}
