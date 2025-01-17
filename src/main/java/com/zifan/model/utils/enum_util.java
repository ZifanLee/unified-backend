package com.zifan.model.utils;

import com.zifan.model.enumType.UserStatus;

public class enum_util {

    public static <E extends Enum<E>> boolean isValidEnumValue(String value, Class<E> enumClass) {
        try {
            E enumValue = Enum.valueOf(enumClass, value);
            // 转换成功，说明是有效字段
            return true;
        } catch (IllegalArgumentException e) {
            // 转换失败，说明不是有效字段
            return false;
        }
    }

    public static void main(String[] args) {
        // 示例枚举
        System.out.println(isValidEnumValue("INVALID", UserStatus.class)); // 假设 Color 是一个枚举
        System.out.println(isValidEnumValue("ONLINE", UserStatus.class)); // 假设 Color 没有 YELLOW
    }
}
