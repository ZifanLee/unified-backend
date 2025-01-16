package com.zifan.dto.request;

import lombok.Data;

@Data
public class SignalRequest {
    private String userEmail; // 用户email
    private String module;    // 模块名称
    private String type;      // 信号类型
    private String id;        // 相关ID
}