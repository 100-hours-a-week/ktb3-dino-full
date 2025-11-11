package com.example.spring_practice.dto;

import com.example.spring_practice.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CommentDto {

    @Getter @Setter
    public static class CreateRequest {
        @NotBlank private String content;
    }

    @Getter @Builder
    public static class Response {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private Long userId;
        private String authorName;

        public static Response from(Comment c) {
            return Response.builder()
                    .id(c.getId())
                    .content(c.getContent())
                    .createdAt(c.getCreatedAt())
                    .userId(c.getAuthor().getId())
                    .authorName(c.getAuthorName())
                    .build();
        }
    }

    @Getter @Builder
    public static class Simple {
        // private Long id;
        // private Long authorId;
        private String authorNickname;
        private String content;
        private LocalDateTime createdAt;
        // private LocalDateTime updatedAt;
    }
}
