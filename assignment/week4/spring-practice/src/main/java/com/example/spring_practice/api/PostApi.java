// api/PostApi.java
package com.example.spring_practice.api;

import com.example.spring_practice.api.dto.*;
import com.example.spring_practice.dto.CommentDto;
import com.example.spring_practice.dto.PostDto;
import com.example.spring_practice.dto.UserDto;
import com.example.spring_practice.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api/posts")
public class PostApi {
    private final PostService posts;
    public PostApi(PostService posts){ this.posts = posts; }

    @GetMapping
    public List<PostListItemOut> list(){
        return posts.list().stream().map(p ->
                new PostListItemOut(p.id(), p.title(), p.author(), p.createdAt(),
                        p.commentCount(), p.likeCount(), p.viewCount())
        ).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id){
        PostDto p = posts.view(id); if (p==null) return notFound().build();
        return ok(toPostOut(p));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PostCreateIn in, HttpSession s){
        UserDto u = (UserDto) s.getAttribute("loginUser");
        PostDto saved = posts.create(in.title(), u.displayName(), in.content(), null);
        return status(201).body(toPostOut(saved));
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<?> like(@PathVariable Long id){
        PostDto p = posts.like(id); if (p==null) return notFound().build();
        return ok(new LikeOut(id, p.likeCount(), true));
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<?> unlike(@PathVariable Long id){
        PostDto p = posts.unlike(id); if (p==null) return notFound().build();
        return ok(new LikeOut(id, p.likeCount(), false));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody CommentIn in, HttpSession s){
        UserDto u = (UserDto) s.getAttribute("loginUser");
        CommentDto c = posts.addComment(id, u.displayName(), in.content());
        if (c==null) return notFound().build();
        return status(201).body(new CommentOut(c.id(), c.postId(), c.author(), c.content(), c.createdAt()));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> listComments(@PathVariable Long id){
        PostDto p = posts.get(id); if (p==null) return notFound().build();
        List<CommentOut> out = p.comments().stream()
                .map(c -> new CommentOut(c.id(), c.postId(), c.author(), c.content(), c.createdAt())).toList();
        return ok(out);
    }

    private PostOut toPostOut(PostDto p){
        var cs = p.comments().stream()
                .map(c -> new CommentOut(c.id(), c.postId(), c.author(), c.content(), c.createdAt())).toList();
        return new PostOut(p.id(), p.title(), p.author(), p.content(), p.createdAt(),
                p.viewCount(), p.likeCount(), p.commentCount(), null, cs);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> edit(@PathVariable Long id,
                                  @RequestBody PostUpdateIn in,
                                  HttpSession session) {
        UserDto me = (UserDto) session.getAttribute("loginUser");
        var status = posts.edit(id, me.displayName(), in.title(), in.content());
        return switch (status) {
            case OK -> noContent().build(); // 204
            case FORBIDDEN -> status(403)
                    .body(new ErrorResponse("FORBIDDEN","작성자만 수정할 수 있습니다.", Instant.now()));
            case NOT_FOUND -> notFound().build();
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpSession session) {
        UserDto me = (UserDto) session.getAttribute("loginUser");
        var status = posts.delete(id, me.displayName());
        return switch (status) {
            case OK -> noContent().build(); // 204
            case FORBIDDEN -> status(403)
                    .body(new ErrorResponse("FORBIDDEN","작성자만 삭제할 수 있습니다.", Instant.now()));
            case NOT_FOUND -> notFound().build();
        };
    }

}
