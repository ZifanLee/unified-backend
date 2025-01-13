package com.zifan.service.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    /**
     * 校验字段长度
     *
     * @param fieldName 字段名称（用于错误提示）
     * @param value     字段值
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @throws IllegalArgumentException 如果字段长度不符合要求
     */
    public static void validateFieldLength(String fieldName, String value, int minLength, int maxLength) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " 不能为空");
        }
        if (value.length() < minLength || value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " 长度必须在 " + minLength + " 到 " + maxLength + " 之间");
        }
    }

    /**
     * 校验字段是否匹配正则表达式
     *
     * @param fieldName 字段名称（用于错误提示）
     * @param value     字段值
     * @param regex     正则表达式
     * @param message   错误提示信息
     * @throws IllegalArgumentException 如果字段值不匹配正则表达式
     */
    public static void validateRegex(String fieldName, String value, String regex, String message) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " 不能为空");
        }
        if (!Pattern.matches(regex, value)) {
            throw new IllegalArgumentException(fieldName + " " + message);
        }
    }

    /**
     * 校验用户名：8-14位，只包含字母和数字
     *
     * @param username 用户名
     * @throws IllegalArgumentException 如果用户名不符合要求
     */
    public static void validateUsername(String username) {
        validateFieldLength("用户名", username, 8, 14);
        validateRegex("用户名", username, "^[a-zA-Z0-9]{8,14}$", "必须由8-14位字母和数字组成");
    }

    /**
     * 校验密码：8-14位，必须包含数字和字母
     *
     * @param password 密码
     * @throws IllegalArgumentException 如果密码不符合要求
     */
    public static void validatePassword(String password) {
        validateFieldLength("密码", password, 8, 14);
        validateRegex("密码", password, "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,14}$", "必须包含数字和字母，且长度为8-14位");
    }

    /**
     * 校验邮箱：符合常见的邮箱格式
     *
     * @param email 邮箱
     * @throws IllegalArgumentException 如果邮箱不符合要求
     */
    public static void validateEmail(String email) {
        validateFieldLength("邮箱", email, 5, 100);
        validateRegex("邮箱", email, "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", "格式不正确");
    }

    /**
     * 校验手机号码：10-20位，只包含数字
     *
     * @param phone 手机号码
     * @throws IllegalArgumentException 如果手机号码不符合要求
     */
    public static void validatePhone(String phone) {
        validateFieldLength("手机号码", phone, 10, 20);
        validateRegex("手机号码", phone, "^[0-9]{10,20}$", "必须由10-20位数字组成");
    }

    /**
     * 校验名字和姓氏：1-50位，只包含字母
     *
     * @param name 名字或姓氏
     * @param fieldName 字段名称（用于错误提示）
     * @throws IllegalArgumentException 如果名字或姓氏不符合要求
     */
    public static void validateName(String name, String fieldName) {
        validateFieldLength(fieldName, name, 1, 50);
        validateRegex(fieldName, name, "^[a-zA-Z]+$", "必须由字母组成");
    }

    /**
     * 校验头像 URL：0-255位，可选字段
     *
     * @param avatarUrl 头像 URL
     * @throws IllegalArgumentException 如果头像 URL 不符合要求
     */
    public static void validateAvatarUrl(String avatarUrl) {
        if (avatarUrl != null) {
            validateFieldLength("头像 URL", avatarUrl, 0, 255);
        }
    }

    /**
     * 校验简介：0-1000位，可选字段
     *
     * @param bio 简介
     * @throws IllegalArgumentException 如果简介不符合要求
     */
    public static void validateBio(String bio) {
        if (bio != null) {
            validateFieldLength("简介", bio, 0, 1000);
        }
    }
}