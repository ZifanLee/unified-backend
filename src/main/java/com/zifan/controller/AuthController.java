package com.zifan.controller;

import com.zifan.dto.request.LoginRequest;
import com.zifan.dto.request.RegisterRequest;
import com.zifan.dto.response.ApiResponse;
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
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;


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

    /**
     * 用户注册接口
     *
     * @param request 注册请求
     * @return 统一响应格式
     */
    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@RequestBody RegisterRequest request) {
        logger.info("用户注册请求: 邮箱 {}", request.getEmail());

        try {
            // 调用 Service 层注册方法
            User user = authService.register(request);
            LoginResponse loginResponse = DtoConverter.ConvertUser2LoginResponse(user);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("email", request.getEmail());
            String token = jwtUtil.generateToken(hashMap);
            loginResponse.setToken(token);

            // 返回成功响应
            logger.info("用户注册成功: 邮箱 {}", request.getEmail());
            return ApiResponse.success(loginResponse, "注册成功");
        } catch (DuplicateUserException | InvalidFieldException | IllegalArgumentException e) {
            // 处理已知异常
            logger.error("用户注册失败: 邮箱 {}, 错误信息: {}", request.getEmail(), e.getMessage());
            return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            // 处理未知异常
            logger.error("用户注册失败: 邮箱 {}, 错误信息: {}", request.getEmail(), e.getMessage());
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "注册失败，请稍后重试");
        }
    }

    /**
     * 用户登录接口
     *
     * @param request 登录请求
     * @return 统一响应格式
     */
    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {
        logger.info("用户登录请求: 邮箱 {}", request.getEmail());

        try {
            // 调用 Service 层登录方法
            String token = authService.login(request);

            // 更新用户状态为在线
            userStatusService.setOnline(request.getEmail());

            // 返回成功响应
            logger.info("用户登录成功: 邮箱 {}", request.getEmail());
            return ApiResponse.success("登录成功", token);
        } catch (UserNotFoundException | InvalidPasswordException | IllegalArgumentException e) {
            // 处理已知异常
            logger.error("用户登录失败: 邮箱 {}, 错误信息: {}", request.getEmail(), e.getMessage());
            return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        } catch (Exception e) {
            // 处理未知异常
            logger.error("用户登录失败: 邮箱 {}, 错误信息: {}", request.getEmail(), e.getMessage());
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "登录失败，请稍后重试");
        }
    }

    @PostMapping("/offline")
    public ApiResponse<String> offline(@RequestBody LoginRequest request) {
        String Email = request.getEmail();
        logger.info("用户请求登出, 请求邮箱 {}", Email);
        try {
            // 获取当前认证对象
            User user = JwtUtil.getAuthenticatedUser();
            if (Email.equals(user.getEmail())) {
                // 更新用户状态离线
                userStatusService.setOffline(Email);
                logger.info("用户登出成功: 邮箱 {}", Email);
                return ApiResponse.success("登出成功");
            } else {
                logger.info("登出失败, 因为非法请求:认证账户:{}和请求登出账户:{}不一致，", user.getEmail(), Email);
                return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "登出失败，请求非法");
            }
        } catch (Exception e) {
            logger.info("用户: {} 登出失败, {}", Email, e.getMessage());
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "登出失败，请稍后重试");
        }
    }


    /**
     * 用户注销接口
     *
     * @param request 注销请求
     * @return 统一响应格式
     */
    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LoginRequest request) {
        String Email = request.getEmail();
        logger.info("用户注销请求: 邮箱 {}", request.getEmail());
        try {
            // 获取当前认证对象
            User user = JwtUtil.getAuthenticatedUser();
            if (Email.equals(user.getEmail())) {
                // 更新用户状态为注销
                userStatusService.setLogout(request.getEmail());
                // 返回成功响应
                logger.info("用户注销成功: 邮箱 {}", request.getEmail());
                return ApiResponse.success("注销成功");
            } else {
                logger.info("注销失败, 请求非法:认证账户:{}和请求注销账户:{}不一致，", user.getEmail(), Email);
                return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "注销失败，请求非法");
            }
        }catch(Exception e){
            logger.error("用户注销失败: 邮箱 {}, 错误信息: {}", request.getEmail(), e.getMessage());
            return ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "注销失败，请稍后重试");
        }
    }
}
