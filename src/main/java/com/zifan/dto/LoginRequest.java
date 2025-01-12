package com.zifan.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String Email;
    private String Id;
    private String password;
}
