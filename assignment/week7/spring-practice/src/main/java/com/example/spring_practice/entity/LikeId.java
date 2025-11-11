package com.example.spring_practice.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class LikeId implements Serializable {
    private Long postId;
    private Long userId;
}
