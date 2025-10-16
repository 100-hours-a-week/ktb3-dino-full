// service/PostService.java
package com.example.spring_practice.service;

import com.example.spring_practice.dto.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    private final PostRepository repo;
    public PostService(PostRepository repo){ this.repo = repo; }

    public List<PostDto> list(){ return repo.findAll(); }
    public PostDto get(Long id){ return repo.findById(id).orElse(null); }
    public PostDto view(Long id){ return repo.incViewAndGet(id).orElse(null); }
    public PostDto create(String title, String author, String content, String imageUrlIgnored){ // image 미사용
        return repo.save(title, author, content);
    }
    public PostDto like(Long id){ return repo.like(id).orElse(null); }
    public PostDto unlike(Long id){ return repo.unlike(id).orElse(null); }
    public CommentDto addComment(Long postId, String author, String content){
        return repo.addComment(postId, author, content).orElse(null);
    }

    public PostRepository.EditStatus edit(Long postId, String newAuthor, String title, String content){
        return repo.edit(postId, newAuthor, title, content);
    }

    public PostRepository.DeleteStatus delete(Long postId, String newAuthor){
        return repo.delete(postId, newAuthor);
    }
}
