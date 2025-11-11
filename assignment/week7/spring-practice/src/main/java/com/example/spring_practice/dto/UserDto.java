package com.example.spring_practice.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class UserDto {
    @Getter @Setter
    public static class SignUpRequest {
        @Email @NotBlank private String email;
        @NotBlank @Size(min=2, max=40) private String nickname;
        @NotBlank @Size(min=4, max=64) private String password;
    }
    @Getter @Builder
    public static class Response {
        private Long id;
        private String email;
        private String nickname;
    }
}
