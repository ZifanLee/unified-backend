package com.zifan.service;

import com.zifan.dto.request.LoginRequest;
import com.zifan.dto.request.RegisterRequest;
import com.zifan.exception.bussiness.DuplicateUserException;
import com.zifan.exception.bussiness.UserNotFoundException;
import com.zifan.exception.validation.InvalidFieldException;
import com.zifan.exception.validation.InvalidPasswordException;
import com.zifan.model.User;
import com.zifan.repository.UserRepository;
import com.zifan.security.JwtUtil;
import com.zifan.service.utils.ValidationUtils;
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
        // 校验字段
        ValidationUtils.validateUsername(request.getUsername());
        ValidationUtils.validatePassword(request.getPassword());
        ValidationUtils.validateEmail(request.getEmail());
        ValidationUtils.validateName(request.getFirstName(), "名字");
        ValidationUtils.validateName(request.getLastName(), "姓氏");
        ValidationUtils.validatePhone(request.getPhone());
        ValidationUtils.validateAvatarUrl(request.getAvatarUrl());
        ValidationUtils.validateBio(request.getBio());

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
        createdUser.setPasswordHash("*".repeat(createdUser.getPasswordHash().length()));
        return createdUser;
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