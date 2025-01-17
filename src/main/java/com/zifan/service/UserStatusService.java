package com.zifan.service;

import com.zifan.controller.AuthController;
import com.zifan.model.User;
import com.zifan.model.utils.enum_util;
import com.zifan.model.enumType.UserStatus;
import com.zifan.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserStatusService {

    private static final String USER_STATUS_KEY_PREFIX = "user:status:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserStatusService.class);

    /**
     * 添加用户状态
     *
     * @param email  用户邮箱
     * @param status 用户状态
     * @return 是否添加成功
     * @throws IllegalArgumentException 如果邮箱或状态非法
     */
    public boolean addUserStatus(String email, String status) {
        validateEmail(email);
        validateStatus(status);

        String key = USER_STATUS_KEY_PREFIX + email;
        ListOperations<String, String> listOps = redisTemplate.opsForList();

        List<String> allStatus = listOps.range(key, 0, -1);
        if (allStatus.contains(status)) {
            logger.info("状态已存在: 用户 {}, 状态 {}", email, status);
            return false;
        }

        listOps.rightPush(key, status);
        logger.info("状态添加成功: 用户 {}, 状态 {}", email, status);
        return true;
    }

    /**
     * 移除用户状态
     *
     * @param email  用户邮箱
     * @param status 用户状态
     * @throws IllegalArgumentException 如果邮箱或状态非法
     */
    public void removeUserStatus(String email, String status) {
        validateEmail(email);
        validateStatus(status);

        String key = USER_STATUS_KEY_PREFIX + email;
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        long removedCount = listOps.remove(key, 0, status); // 移除所有匹配的状态

        if (removedCount > 0) {
            logger.info("状态移除成功: 用户 {}, 状态 {}, 移除数量 {}", email, status, removedCount);
        } else {
            logger.info("状态不存在: 用户 {}, 状态 {}", email, status);
        }
    }

    /**
     * 获取用户所有状态
     *
     * @param email 用户邮箱
     * @return 用户状态列表
     * @throws IllegalArgumentException 如果邮箱非法
     */
    public List<String> getUserStatuses(String email) {
        validateEmail(email);

        String key = USER_STATUS_KEY_PREFIX + email;
        List<String> statuses = redisTemplate.opsForList().range(key, 0, -1);

        logger.info("获取用户状态: 用户 {}, 状态列表 {}", email, statuses);
        return statuses;
    }

    /**
     * 清空用户所有状态
     *
     * @param email 用户邮箱
     * @throws IllegalArgumentException 如果邮箱非法
     */
    public void clearUserStatuses(String email) {
        validateEmail(email);

        String key = USER_STATUS_KEY_PREFIX + email;
        Boolean isDeleted = redisTemplate.delete(key);

        if (isDeleted != null && isDeleted) {
            logger.info("状态清空成功: 用户 {}", email);
        } else {
            logger.info("状态清空失败: 用户 {}", email);
        }
    }

    /**
     * 校验邮箱是否合法
     *
     * @param email 用户邮箱
     * @throws IllegalArgumentException 如果邮箱非法
     */
    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            logger.error("邮箱为空");
            throw new IllegalArgumentException("邮箱不能为空");
        }

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            logger.error("邮箱不存在: {}", email);
            throw new IllegalArgumentException("邮箱不存在: " + email);
        }
    }

    /**
     * 校验状态是否合法
     *
     * @param status 用户状态
     * @throws IllegalArgumentException 如果状态非法
     */
    private void validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            logger.error("状态为空");
            throw new IllegalArgumentException("状态不能为空");
        }

        if (!enum_util.isValidEnumValue(status, UserStatus.class)) {
            logger.error("非法状态: {}", status);
            throw new IllegalArgumentException("非法状态: " + status);
        }
    }
}