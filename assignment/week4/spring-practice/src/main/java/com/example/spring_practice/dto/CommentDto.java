// dto/CommentDto.java
package com.example.spring_practice.dto;
import java.time.LocalDateTime;
public record CommentDto(Long id, Long postId, String author, String content, LocalDateTime createdAt) {}
