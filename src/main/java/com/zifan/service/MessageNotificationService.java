package com.zifan.service;

import com.zifan.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageNotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 发送私聊消息通知给指定用户
     *
     * @param userId  目标用户 ID
     * @param message 消息内容
     */
    public void notifyPrivateMessage(String userId, Message message) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/private-messages", message);
    }

    /**
     * 发送群聊消息通知给指定群组
     *
     * @param groupId 群组 ID
     * @param message 消息内容
     */
    public void notifyGroupMessage(String groupId, Message message) {
        messagingTemplate.convertAndSend("/topic/group-messages/" + groupId, message);
    }
}