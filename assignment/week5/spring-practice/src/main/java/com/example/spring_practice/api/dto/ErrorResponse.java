// api/dto/ErrorResponse.java
package com.example.spring_practice.api.dto;
import java.time.Instant;
public record ErrorResponse(String error, String message, Instant timestamp) {}
