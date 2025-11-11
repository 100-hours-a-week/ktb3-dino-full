package com.example.spring_practice.controller;

import com.example.spring_practice.dto.CommentDto;
import com.example.spring_practice.entity.Comment;
import com.example.spring_practice.repository.CommentRepository;
import com.example.spring_practice.service.CommentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.spring_practice.controller.AuthController.LOGIN_USER_ID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;
    private final CommentRepository commentRepository;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto.Response> add(
            @PathVariable Long postId,
            @RequestBody @Valid CommentDto.CreateRequest req,
            HttpSession session) {
        Long authorId = (Long) session.getAttribute(LOGIN_USER_ID);
        Comment c = commentService.addComment(postId, authorId, req.getContent());
        return ResponseEntity.ok(CommentDto.Response.from(c));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto.Response>> list(@PathVariable Long postId) {
        List<Comment> list = commentRepository.findByPost_IdOrderByCreatedAtAsc(postId);
        return ResponseEntity.ok(list.stream().map(CommentDto.Response::from).toList());
    }


    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        commentService.removeComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
