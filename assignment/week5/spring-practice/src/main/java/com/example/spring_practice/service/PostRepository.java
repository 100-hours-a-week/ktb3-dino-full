// service/PostRepository.java
package com.example.spring_practice.service;

import com.example.spring_practice.dto.*;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    public enum EditStatus {NOT_FOUND, FORBIDDEN, OK}
    public enum DeleteStatus {NOT_FOUND, FORBIDDEN, OK}

    private static class PostEntity {
        Long id; String title; String author; String content;
        LocalDateTime createdAt; Long userid;
        Long viewCount  = 0L; Long likeCount = 0L;
        final List<CommentDto> comments = new ArrayList<>();
        // final Set<Long> comments= ConcurrentHashMap.newKeySet();

        PostEntity(Long id,Long userid, String title, String author, String content, LocalDateTime createdAt){
            this.id=id; this.userid = userid; this.title=title; this.author=author; this.content=content; this.createdAt=createdAt;
        }
        synchronized void incView(){ viewCount++; }
        synchronized void like(){ likeCount++; }
        synchronized void unlike(){ if(likeCount>0) likeCount--; }

        synchronized CommentDto addComment(Long cid, String author, String content){
            var c = new CommentDto(cid, id, author, content, LocalDateTime.now());
            comments.add(c); return c;
        }

        synchronized EditStatus edit(String newAuthor, String newTitle, String newContent){
            if(!author.equals(newAuthor)) return EditStatus.FORBIDDEN;
            this.title = newTitle; this.content = newContent;
            return EditStatus.OK;
        }

        synchronized PostDto toDto() {
            return new PostDto(id, title, author, content, createdAt, viewCount, likeCount,
                    comments.size(), List.copyOf(comments));
        }
    }

    private final Map<Long, PostEntity> store = new ConcurrentHashMap<>();
    private final AtomicLong postSeq = new AtomicLong(0);
    private final AtomicLong commentSeq = new AtomicLong(0);

    public PostRepository(){
        save("Welcome!", 0L, "admin", "I love seoul Korea. Kimchi delicious");
    }

    public List<PostDto> findAll(){
        List<PostDto> list = new ArrayList<>();
        for (var e: store.values()) list.add(e.toDto());
        list.sort(Comparator.comparing(PostDto::createdAt).reversed());
        return list;
    }

    public Optional<PostDto> findById(Long id){
        var e = store.get(id);
        return Optional.ofNullable(e).map(PostEntity::toDto);
    }

    public PostDto save(String title,Long userid, String author, String content){
        Long id = postSeq.incrementAndGet();
        var e = new PostEntity(id,userid, title, author, content, LocalDateTime.now());
        store.put(id, e);
        return e.toDto();
    }

    public Optional<PostDto> incViewAndGet(Long id){
        var e = store.get(id); if (e==null) return Optional.empty();
        e.incView(); return Optional.of(e.toDto());
    }

    public Optional<PostDto> like(Long id){
        var e = store.get(id); if (e==null) return Optional.empty();
        e.like(); return Optional.of(e.toDto());
    }

    public Optional<PostDto> unlike(Long id){
        var e = store.get(id); if (e==null) return Optional.empty();
        e.unlike(); return Optional.of(e.toDto());
    }

    public Optional<CommentDto> addComment(Long postId, String author, String content){
        var e = store.get(postId); if (e==null) return Optional.empty();
        Long cid = commentSeq.incrementAndGet();
        return Optional.of(e.addComment(cid, author, content));
    }

    public EditStatus edit(Long postId, Long userid, String newAuthor, String newTitle, String content) {
        var e = store.get(postId);
        if (e == null) return EditStatus.NOT_FOUND;
        if(!e.userid.equals(userid)) return EditStatus.FORBIDDEN;
        e.author = newAuthor; e.title = newTitle; e.content = content;
        return EditStatus.OK;
    }

    public void updateAuthor(Long postId, String newAuthor){
        var e = store.get(postId); if (e==null) return;
        e.author = newAuthor;
    }

    public DeleteStatus delete(Long postId, Long userid) {
        var e = store.get(postId);
        if (e == null) return DeleteStatus.NOT_FOUND;
        if(!e.userid.equals(userid)) {
            return DeleteStatus.FORBIDDEN;
        }
        store.remove(postId);
        return DeleteStatus.OK;
    }

}
