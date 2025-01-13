package com.zifan.controller;

import com.zifan.model.Message;
import com.zifan.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private MessageService messageService;

    // 发送私聊消息
    @MessageMapping("/chat.sendPrivateMessage")
    public void sendPrivateMessage(String content, @Header("senderId") String senderId, @Header("receiverId") String receiverId) {
        messageService.sendPrivateMessage(senderId, receiverId, content);
    }

    // 发送群聊消息
    @MessageMapping("/chat.sendGroupMessage")
    public void sendGroupMessage(String content, @Header("senderId") String senderId, @Header("groupId") String groupId) {
        messageService.sendGroupMessage(senderId, groupId, content);
    }

    // 分页查询私聊消息
    @GetMapping("/private-messages")
    public Page<Message> getPrivateMessages(
            @RequestParam String senderId,
            @RequestParam String receiverId,
            Pageable pageable) {
        return messageService.getPrivateMessages(senderId, receiverId, pageable);
    }

    // 分页查询群聊消息
    @GetMapping("/group-messages")
    public Page<Message> getGroupMessages(
            @RequestParam String groupId,
            Pageable pageable) {
        return messageService.getGroupMessages(groupId, pageable);
    }

    // 撤回消息
    @PostMapping("/recall-message")
    public void recallMessage(@RequestParam String messageId) {
        messageService.recallMessage(messageId);
    }

    // 编辑消息
    @PostMapping("/edit-message")
    public void editMessage(@RequestParam String messageId, @RequestParam String newContent) {
        messageService.editMessage(messageId, newContent);
    }
}