package com.example.spring_practice.dto;


import com.example.spring_practice.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder
public class PostDto {

    @Getter @Setter
    public static class CreateRequest {
        @NotBlank @Size(max=200) private String title;
        @NotBlank private String content;
        @Size(max=300) private String imageUrl;
    }

    @Getter @Builder
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private String imageUrl;
        private int viewCount;
        private int commentsCount;
        private int likesCount;
        private LocalDateTime createdAt;
        private Long authorId;
        private String authorName;

        public static Response from(Post p) {
            return Response.builder()
                    .id(p.getId())
                    .title(p.getTitle())
                    .content(p.getContent())
                    .imageUrl(p.getImageUrl())
                    .viewCount(p.getViewCount())
                    .commentsCount(p.getCommentsCount())
                    .likesCount(p.getLikesCount())
                    .createdAt(p.getCreatedAt())
                    .authorId(p.getAuthor().getId())
                    .authorName(p.getAuthorName())
                    .build();
        }
    }

    @Getter @Builder
    public static class Summary {
        // private Long id;
        private String title;
        private int viewCount;
        private int commentsCount;
        private int likesCount;
        private LocalDateTime createdAt;
        private String authorName;

        public static Summary from(Post p) {
            return Summary.builder()
                    // .id(p.getId())
                    .title(p.getTitle())
                    .viewCount(p.getViewCount())
                    .commentsCount(p.getCommentsCount())
                    .likesCount(p.getLikesCount())
                    .createdAt(p.getCreatedAt())
                    .authorName(p.getAuthorName())
                    .build();
        }
    }

    @Getter @Builder
    public static class Detail {
        // private Long id;
        private String title;
        private String content;
        private String imageUrl;

        // private Long authorId;
        private String authorNickname;

        private long likeCount;
        private long commentCount;

        private long views;
        private LocalDateTime createdAt;
        // private LocalDateTime updatedAt;

        private List<CommentDto.Simple> comments;
    }


}
