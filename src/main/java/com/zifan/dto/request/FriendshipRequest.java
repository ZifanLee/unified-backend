package com.zifan.dto.request;

import lombok.Data;

@Data
public class FriendshipRequest {
    private String userEmail;
    private String friendEmail;
    private String message; // 可选，用于发送好友请求时的附加消息

}