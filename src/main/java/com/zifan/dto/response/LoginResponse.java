package com.zifan.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public LoginResponse() {
    }
}