package com.zifan.controller;

import com.zifan.aop.LoggingAspect;
import com.zifan.dto.request.LoginRequest;
import com.zifan.dto.request.RegisterRequest;
import com.zifan.dto.response.LoginResponse;
import com.zifan.dto.utils.DtoConverter;
import com.zifan.exception.bussiness.DuplicateUserException;
import com.zifan.exception.bussiness.UserNotFoundException;
import com.zifan.exception.validation.InvalidFieldException;
import com.zifan.exception.validation.InvalidPasswordException;
import com.zifan.security.JwtUtil;
import com.zifan.service.AuthService;
import com.zifan.model.User;
import com.zifan.service.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private UserStatusService userStatusService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        boolean hasException = false;
        try {
            // 调用 Service 层注册方法
            User user = authService.register(request);
            LoginResponse loginResponse = DtoConverter.ConvertUser2LoginResponse(user);
            Map<String, Object> response = new HashMap<>();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("email", request.getEmail());
            String token = jwtUtil.generateToken(hashMap);
            loginResponse.setToken(token);

            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "注册成功");
            response.put("data", loginResponse); // 返回注册的用户信息

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicateUserException e) {
            hasException = true;
            // 邮箱重复
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (InvalidFieldException e) {
            hasException = true;
            // 字段校验失败
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IllegalArgumentException e) {
            hasException = true;
            // 非法参数
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            hasException = true;
            // 其他异常
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "注册失败，请稍后重试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } finally {
            if (hasException) {
                logger.info("用户 {} 注册失败", request.getEmail());
            } else {
                logger.info("用户 {} 注册成功", request.getEmail());
            }
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
            // 更新用户状态， todo: 还需要设计登出，以及状态不重复的检查
            userStatusService.addUserStatus(request.getEmail(), "online");
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
