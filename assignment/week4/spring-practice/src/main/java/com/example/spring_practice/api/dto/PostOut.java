// api/dto/PostOut.java
package com.example.spring_practice.api.dto;
import java.time.LocalDateTime;
import java.util.List;
public record PostOut(
        Long id, String title, String author, String content, LocalDateTime createdAt,
        long viewCount, long likeCount, long commentCount, String imageUrl, // null
        List<CommentOut> comments
) {}