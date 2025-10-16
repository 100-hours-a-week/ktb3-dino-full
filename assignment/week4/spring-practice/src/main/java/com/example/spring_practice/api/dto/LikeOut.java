// api/dto/LikeOut.java
package com.example.spring_practice.api.dto;
public record LikeOut(Long postId, long likeCount, boolean liked) {}