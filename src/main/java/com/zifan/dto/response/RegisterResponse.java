package com.zifan.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterResponse {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Integer status;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
    private String avatarUrl;
    private String bio;
    private String token;
}
