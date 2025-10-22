// dto/PostDto.java
package com.example.spring_practice.dto;
import java.time.LocalDateTime;
import java.util.List;

public record PostDto(
        Long id, String title, String author, String content, LocalDateTime createdAt,
        long viewCount, long likeCount, long commentCount, List<CommentDto> comments
) {}
