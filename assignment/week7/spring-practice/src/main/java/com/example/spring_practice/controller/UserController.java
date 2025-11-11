package com.example.spring_practice.controller;

import com.example.spring_practice.dto.UserDto;
import com.example.spring_practice.entity.User;
import com.example.spring_practice.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<UserDto.Response> signUp(@RequestBody @Valid UserDto.SignUpRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().build();
        }
        User user = userRepository.save(User.builder()
                .email(req.getEmail())
                .nickname(req.getNickname())
                .password(req.getPassword())
                .build());
        return ResponseEntity.ok(
                UserDto.Response.builder()
                        .id(user.getId()).email(user.getEmail()).nickname(user.getNickname()).build()
        );
    }

}
