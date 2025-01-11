package com.zifan.controller;

import com.zifan.dto.LoginRequest;
import com.zifan.dto.RegisterRequest;
import com.zifan.service.AuthService;
import com.zifan.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // 注册
    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    // 登录
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        if (authService.login(request)) {
            return "登录成功";
        } else {
            return "用户名或密码错误";
        }
    }
}
