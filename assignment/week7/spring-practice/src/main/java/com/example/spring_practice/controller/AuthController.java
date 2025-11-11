package com.example.spring_practice.controller;

import com.example.spring_practice.dto.AuthDto;
import com.example.spring_practice.dto.UserDto;
import com.example.spring_practice.entity.User;
import com.example.spring_practice.repository.UserRepository;
import com.example.spring_practice.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    public static final String LOGIN_USER_ID = "LOGIN_USER_ID";
    private final UserRepository userRepository;
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserDto.Response> join(@RequestBody @Valid UserDto.SignUpRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .build();
        }

        User user = userRepository.save(User.builder()
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .build());

        return ResponseEntity
                .created(URI.create("/api/users/" + user.getId()))
                .body(UserDto.Response.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthDto.LoginRequest req, HttpSession session) {
        var user = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (user == null || !user.getPassword().equals(req.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error","UNAUTHORIZED","message","invalid credentials"));
        }
        session.setAttribute(LOGIN_USER_ID, user.getId());
        return ResponseEntity.ok(
                AuthDto.LoginResponse.builder()
                        .userId(user.getId())
                        .nickname(user.getNickname())
                        .email(user.getEmail())
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Long uid = (Long) session.getAttribute(LOGIN_USER_ID);
        if (uid == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error","UNAUTHORIZED","message","not logged in"));
        return userRepository.findById(uid)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(Map.of(
                        "userId", u.getId(), "nickname", u.getNickname(), "email", u.getEmail()
                ))).orElseGet(() -> {
                    session.invalidate();
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Map.of("error","UNAUTHORIZED","message","user not found"));
                });
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMe(HttpSession session) {
        Long uid = (Long) session.getAttribute(LOGIN_USER_ID);
        if (uid == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        userService.deleteAccount(uid);
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }




}
