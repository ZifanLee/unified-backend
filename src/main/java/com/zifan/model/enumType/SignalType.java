package com.zifan.model.enumType;

public enum SignalType {
    // 好友模块
    FRIEND_REQUEST,  // 好友请求
    FRIEND_ACCEPT,   // 好友请求被接受
    FRIEND_REJECT,   // 好友请求被拒绝

    // 消息模块
    NEW_MESSAGE,     // 新消息
    MESSAGE_READ,    // 消息已读
    MESSAGE_RECALL,  // 消息撤回

    // 其他模块
}

