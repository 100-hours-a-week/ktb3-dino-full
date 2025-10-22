package com.example.spring_practice.service;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CommentRepository {
    public enum EditStatus {NOT_FOUND, FORBIDDEN, OK}
    public enum DeleteStatus {NOT_FOUND, FORBIDDEN, OK}

    private static class CommentEntity{
        Long id; Long userid; String Author; String content;
        LocalDateTime createdAt; Long postid;
        CommentEntity(Long id, Long postid, Long userid, String Author, String content, LocalDateTime createdAt) {
            this.id = id;
            this.postid = postid;
            this.userid = userid;
            this.Author = Author;
            this.content = content;
            this.createdAt = createdAt;
        }
    }

    private final Map<Long, CommentEntity> byIdMap = new ConcurrentHashMap<>();
    private final AtomicLong commentSeq = new AtomicLong(0);



}
