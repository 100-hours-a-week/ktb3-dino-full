package com.example.spring_practice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes",
        uniqueConstraints = @UniqueConstraint(name = "ux_likes_post_user", columnNames = {"postId","userId"}),
        indexes = {
                @Index(name = "idx_likes_post", columnList = "postId"),
                @Index(name = "idx_likes_user", columnList = "userId")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"post","user"})
public class Like {

    @EmbeddedId
    private com.example.spring_practice.entity.LikeId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("postId")
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("userId")
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public static Like of(Post post, User user) {
        return Like.builder()
                .id(new LikeId(post.getId(), user.getId()))
                .post(post)
                .user(user)
                .build();
    }
}
