package com.example.spring_practice.controller;


import com.example.spring_practice.dto.LikeDto;
import com.example.spring_practice.dto.PostDto;
import com.example.spring_practice.entity.Post;
import com.example.spring_practice.repository.PostRepository;
import com.example.spring_practice.service.LikeService;
import com.example.spring_practice.service.NotFoundException;
import com.example.spring_practice.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static com.example.spring_practice.controller.AuthController.LOGIN_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<PostDto.Response> create(@RequestBody @Valid PostDto.CreateRequest req, HttpSession session) {
        Long authorId = (Long) session.getAttribute(LOGIN_USER_ID);
        Post p = postService.createPost(authorId, req.getTitle(), req.getContent(), req.getImageUrl());
        return ResponseEntity.ok(PostDto.Response.from(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto.Detail> detail(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getDetail(id));
    }
    @GetMapping
    public ResponseEntity<List<PostDto.Summary>> listAll(
            @RequestParam(defaultValue = "createdAt,DESC") String sort) {

        Sort s = sort.contains(",")
                ? Sort.by(Sort.Order.by(sort.split(",")[0])
                .with("DESC".equalsIgnoreCase(sort.split(",")[1])
                        ? Sort.Direction.DESC : Sort.Direction.ASC))
                : Sort.by(Sort.Order.desc("createdAt"));

        List<Post> posts = postRepository.findAll(s);
        return ResponseEntity.ok(posts.stream().map(PostDto.Summary::from).toList());
    }


    @PostMapping("/{id}/likes")
    public ResponseEntity<LikeDto.ToggleResponse> toggleLike(
            @PathVariable Long id,
            HttpSession session) {

        Long userId = (Long) session.getAttribute(LOGIN_USER_ID);

        boolean liked = likeService.toggleLike(id, userId);
        int likesCount = postRepository.findById(id).orElseThrow(()
                        -> new NotFoundException("Post not found: " + id)).getLikesCount();

        return ResponseEntity.ok(LikeDto.ToggleResponse.builder().liked(liked).likesCount(likesCount).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpSession session) throws AccessDeniedException {

        Long uid = (Long) session.getAttribute(LOGIN_USER_ID);
        if (uid == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        postService.deletePost(id, uid);
        return ResponseEntity.noContent().build(); // 204
    }
}
