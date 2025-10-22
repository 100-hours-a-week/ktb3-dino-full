// api/AuthApi.java
package com.example.spring_practice.api;

import com.example.spring_practice.api.dto.*;
import com.example.spring_practice.dto.UserDto;
import com.example.spring_practice.service.AuthService;
import com.example.spring_practice.service.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("/api")
public class AuthApi {
    private final UserRepository users;
    private final AuthService auth;
    public AuthApi(UserRepository users, AuthService auth){
        this.users=users;
        this.auth=auth;
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinIn in){
        if (in.username()==null || in.password()==null || in.displayName()==null)
            return badRequest().body(new ErrorResponse("BAD_REQUEST","필수값 누락", Instant.now()));
        if (users.existsByUsername(in.username()))
            return status(409).body(new ErrorResponse("USERNAME_TAKEN","이미 사용 중", Instant.now()));
        UserDto u = users.save(in.username(), in.password(), in.displayName());
        return status(201).body(new UserOut(u.id(), u.userName(), u.displayName()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginIn in, HttpSession s){
        var u = auth.login(in.username(), in.password());
        if (u==null)
            return status(401).body(new ErrorResponse("INVALID_CREDENTIALS","아이디/비밀번호 오류", Instant.now()));
        s.setAttribute("loginUser", u);
        return ok(new UserOut(u.id(), u.userName(), u.displayName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession s){
        s.invalidate(); return noContent().build();
    }
}
