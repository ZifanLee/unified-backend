package com.zifan.controller;

import com.zifan.dto.LoginRequest;
import com.zifan.dto.RegisterRequest;
import com.zifan.exception.bussiness.DuplicateUserException;
import com.zifan.exception.bussiness.UserNotFoundException;
import com.zifan.exception.validation.InvalidFieldException;
import com.zifan.exception.validation.InvalidPasswordException;
import com.zifan.security.JwtUtil;
import com.zifan.service.AuthService;
import com.zifan.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // 调用 Service 层注册方法
            User user = authService.register(request);
            // 返回注册成功的响应
            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "注册成功");
            response.put("data", user); // 返回注册的用户信息

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("email", request.getEmail());
            String token = jwtUtil.generateToken(hashMap);
            response.put("token", token);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicateUserException e) {
            // 邮箱重复
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (InvalidFieldException e) {
            // 字段校验失败
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // 其他异常
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "注册失败，请稍后重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 调用 Service 层登录方法
            String token = authService.login(request);

            // 返回 Token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            // 用户不存在
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登录失败，请稍后重试");
        }
    }
}
