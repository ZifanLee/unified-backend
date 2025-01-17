package com.zifan.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class UserStatusResponse {
    // 用户email
    private String userEmail;
    // 用户状态集合
    private List<String> status;
    // 标志字段
    private int flag;
    // 附加信息
    private String message;
}
