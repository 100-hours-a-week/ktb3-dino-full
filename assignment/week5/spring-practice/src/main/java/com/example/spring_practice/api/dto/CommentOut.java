// api/dto/CommentOut.java
package com.example.spring_practice.api.dto;
import java.time.LocalDateTime;
public record CommentOut(Long id, Long postId, String author, String content, LocalDateTime createdAt) {}
