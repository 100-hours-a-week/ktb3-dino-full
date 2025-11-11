package com.example.spring_practice.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class AuthDto {
    @Getter @Setter
    public static class LoginRequest {
        @NotBlank private String email;
        @NotBlank private String password;
    }
    @Getter @Builder
    public static class LoginResponse {
        private Long userId;
        private String nickname;
        private String email;
    }
}
