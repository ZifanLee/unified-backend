package com.zifan.service;

import com.zifan.dto.LoginRequest;
import com.zifan.dto.RegisterRequest;
import com.zifan.exception.bussiness.DuplicateUserException;
import com.zifan.exception.bussiness.UserNotFoundException;
import com.zifan.exception.validation.InvalidFieldException;
import com.zifan.exception.validation.InvalidPasswordException;
import com.zifan.model.User;
import com.zifan.repository.UserRepository;
import com.zifan.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 注册
    public User register(RegisterRequest request) {
        // 校验字段长度
        validateFieldLength("用户名", request.getUsername(), 3, 50);
        validateFieldLength("邮箱", request.getEmail(), 5, 100);
        validateFieldLength("密码", request.getPassword(), 6, 255);
        validateFieldLength("名字", request.getFirstName(), 1, 50);
        validateFieldLength("姓氏", request.getLastName(), 1, 50);
        validateFieldLength("手机号码", request.getPhone(), 10, 20);
        validateFieldLength("头像 URL", request.getAvatarUrl(), 0, 255);
        validateFieldLength("简介", request.getBio(), 0, 1000);

        // 检查邮箱是否已存在
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateUserException("邮箱已存在");
        }

        // 创建用户对象
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setBio(request.getBio());
        User createdUser = userRepository.save(user);
        // todo: 密码隐藏
        return createdUser;
    }

    // 校验字段长度
    private void validateFieldLength(String fieldName, String value, int minLength, int maxLength) {
        if (!StringUtils.hasText(value)) {
            if (minLength > 0) {
                throw new InvalidFieldException(fieldName + "不能为空");
            }
            return; // 允许空值
        }
        if (value.length() < minLength || value.length() > maxLength) {
            throw new InvalidFieldException(fieldName + "长度必须在 " + minLength + " 到 " + maxLength + " 之间");
        }
    }

    // 登录
    public String login(LoginRequest request) {
        // 查找用户
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Email", request.getEmail()));

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("用户不存在或密码错误");
        }

        // 更新最后登录时间
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", request.getEmail());

        // 生成 JWT Token
        return jwtUtil.generateToken(hashMap);

    }
}