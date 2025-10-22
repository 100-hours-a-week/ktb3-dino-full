// api/dto/PostListItemOut.java
package com.example.spring_practice.api.dto;
import java.time.LocalDateTime;
public record PostListItemOut(
        Long id, String title, String author, LocalDateTime createdAt,
        long commentCount, long likeCount, long viewCount// imageUrl 현재 null
) {}
