package com.zifan.dto.response;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private int status;       // 状态码
    private String message;   // 附带消息
    private T data;           // DTO数据

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }

    // 静态工厂方法：成功响应
    public static <T> ApiResponse<T> success(T data, String Message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage(Message);
        response.setData(data);
        return response;
    }

    // 静态工厂方法：错误响应
    public static <T> ApiResponse<T> error(int status, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMessage(message);
        return response;
    }

    public static <T> ApiResponse<T> error(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(400);
        response.setMessage(message);
        return response;
    }
}
