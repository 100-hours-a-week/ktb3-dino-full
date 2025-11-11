package com.example.spring_practice.service;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) { super(msg); }
}
