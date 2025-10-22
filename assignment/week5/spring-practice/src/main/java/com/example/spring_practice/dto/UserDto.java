// dto/UserDto.java
package com.example.spring_practice.dto;

public record UserDto(
        Long id, String userName,
        String password, String displayName
) {}
