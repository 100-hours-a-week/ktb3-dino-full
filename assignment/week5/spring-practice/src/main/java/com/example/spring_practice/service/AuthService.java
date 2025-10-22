// service/AuthService.java
package com.example.spring_practice.service;

import com.example.spring_practice.dto.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {
    private final UserRepository users;
    private final PostRepository posts;

    public AuthService(UserRepository users, PostRepository posts){
        this.users = users;
        this.posts = posts;
    }

    public UserDto login(String username, String password){
        var u = users.findByUsername(username);
        if (u==null) return null;
        return u.password().equals(password) ? u : null;
    }

    public void deleteCurrentAccount(HttpSession session) {
        var me = (UserDto) session.getAttribute("loginUser");
        if (me == null) return;             // 비로그인 상태
        Set<Long> AuthoredPost = users.getAuthoredPosts(me.id());
        Set<Long> LikedPost= users.getLikedPosts(me.id());
        users.deleteById(me.id());       // 계정 삭제

        for (Long postid : AuthoredPost) {
            posts.delete(postid, me.id());
            // delete post
        }
        for (Long postid : LikedPost) {
            posts.unlike(postid);
            // unlike post
        }
    }

    public UserDto updateCurrentAccountDisplayName(HttpSession session, String newDisplayName) {
        var me = (UserDto) session.getAttribute("loginUser");
        Set<Long> AuthoredPost = users.getAuthoredPosts(me.id());
        // comments
        for (Long postid : AuthoredPost) {
            posts.updateAuthor(postid, newDisplayName);
        }
        return users.updateDisplayName(me.id(),newDisplayName);
    }

}
