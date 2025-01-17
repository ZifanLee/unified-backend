package com.zifan.controller;

import com.zifan.dto.response.ApiResponse;
import com.zifan.dto.response.UserStatusResponse;
import com.zifan.service.UserStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-status")
public class UserStatusController {

    @Autowired
    private UserStatusService userStatusService;

    private static final Logger logger = LoggerFactory.getLogger(UserStatusController.class);

    /**
     * 添加用户状态
     *
     * @param email  用户邮箱
     * @param status 用户状态
     * @return ApiResponse<UserStatusResponse>
     */
    @PostMapping("/add")
    public ApiResponse<UserStatusResponse> addStatus(
            @RequestParam("userEmail") String email,
            @RequestParam("status") String status) {
        logger.info("添加用户状态请求: 用户 {}, 状态 {}", email, status);
        try {
            boolean success = userStatusService.addUserStatus(email, status);
            UserStatusResponse userStatusResponse = new UserStatusResponse();
            if (success) {
                userStatusResponse.setMessage("状态添加成功");
                logger.info("状态添加成功: 用户 {}, 状态 {}", email, status);
            } else {
                userStatusResponse.setMessage("状态已存在，幂等操作");
                logger.info("状态已存在: 用户 {}, 状态 {}", email, status);
            }
            return ApiResponse.success(userStatusResponse);
        } catch (Exception e) {
            logger.error("添加用户状态失败: 用户 {}, 状态 {}, 错误信息: {}", email, status, e.getMessage());
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 移除用户状态
     *
     * @param email  用户邮箱
     * @param status 用户状态
     * @return ApiResponse<UserStatusResponse>
     */
    @DeleteMapping("/remove")
    public ApiResponse<UserStatusResponse> removeStatus(
            @RequestParam("userEmail") String email,
            @RequestParam("status") String status) {
        logger.info("移除用户状态请求: 用户 {}, 状态 {}", email, status);
        try {
            userStatusService.removeUserStatus(email, status);
            UserStatusResponse userStatusResponse = new UserStatusResponse();
            userStatusResponse.setMessage("状态移除成功");
            logger.info("状态移除成功: 用户 {}, 状态 {}", email, status);
            return ApiResponse.success(userStatusResponse);
        } catch (Exception e) {
            logger.error("移除用户状态失败: 用户 {}, 状态 {}, 错误信息: {}", email, status, e.getMessage());
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 获取用户所有状态
     *
     * @param email 用户邮箱
     * @return ApiResponse<List<String>>
     */
    @GetMapping("/getAll")
    public ApiResponse<List<String>> getStatuses(
            @RequestParam("userEmail") String email) {
        logger.info("获取用户状态请求: 用户 {}", email);
        try {
            List<String> statuses = userStatusService.getUserStatuses(email);
            logger.info("获取用户状态成功: 用户 {}, 状态列表 {}", email, statuses);
            return ApiResponse.success(statuses);
        } catch (Exception e) {
            logger.error("获取用户状态失败: 用户 {}, 错误信息: {}", email, e.getMessage());
            return ApiResponse.error(400, e.getMessage());
        }
    }

    /**
     * 清空用户所有状态
     *
     * @param email 用户邮箱
     * @return ApiResponse<UserStatusResponse>
     */
    @DeleteMapping("/removeAll")
    public ApiResponse<UserStatusResponse> clearStatuses(
            @RequestParam("userEmail") String email) {
        logger.info("清空用户状态请求: 用户 {}", email);
        try {
            userStatusService.clearUserStatuses(email);
            UserStatusResponse userStatusResponse = new UserStatusResponse();
            userStatusResponse.setMessage("状态清空成功");
            logger.info("状态清空成功: 用户 {}", email);
            return ApiResponse.success(userStatusResponse);
        } catch (Exception e) {
            logger.error("清空用户状态失败: 用户 {}, 错误信息: {}", email, e.getMessage());
            return ApiResponse.error(400, e.getMessage());
        }
    }
}