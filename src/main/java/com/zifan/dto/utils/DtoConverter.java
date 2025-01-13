package com.zifan.dto.utils;

import com.zifan.dto.response.LoginResponse;
import com.zifan.model.User;

public class DtoConverter {

    public static LoginResponse ConvertUser2LoginResponse(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setFirstName(user.getFirstName());
        loginResponse.setLastName(user.getLastName());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setStatus(user.getStatus());
        loginResponse.setRole(user.getRole());
        loginResponse.setCreatedAt(user.getCreatedAt());
        loginResponse.setUpdatedAt(user.getUpdatedAt());
        loginResponse.setLastLoginAt(user.getLastLoginAt());
        loginResponse.setAvatarUrl(user.getAvatarUrl());
        loginResponse.setBio(user.getBio());
        return loginResponse;
    }
}
