package com.example.spring_practice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts",
        indexes = {
                @Index(name = "idx_posts_user", columnList = "userId"),
                @Index(name = "idx_posts_createdAt", columnList = "createdAt")
        })
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(exclude = {"author", "comments", "likes"})
@EqualsAndHashCode(of = "id")
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId")
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob @Column(nullable = false)
    private String content;

    @Column(length = 300)
    private String imageUrl;

    @Column(name = "views", nullable = false)
    @Builder.Default
    private int viewCount = 0;

    @Column(name = "comments", nullable = false)
    @Builder.Default
    private int commentsCount = 0;

    @Column(name = "likes", nullable = false)
    @Builder.Default
    private int likesCount = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 80)
    private String authorName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private Set<Like> likes = new HashSet<>();

    public void setAuthor(User author) {
        this.author = author;
        this.authorName = (author != null ? author.getNickname() : null);
        if (author != null) author.getPosts().add(this);
    }

    public void increaseViews() { if (viewCount < Integer.MAX_VALUE) viewCount++; }
}
