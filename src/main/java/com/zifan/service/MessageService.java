package com.zifan.service;

import com.zifan.model.Message;
import com.zifan.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // 发送私聊消息
    @Transactional
    public Message sendPrivateMessage(String senderId, String receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setType("text");
        message.setStatus("sent");
        message.setSentAt(new Date());
        return messageRepository.save(message);
    }

    // 发送群聊消息
    @Transactional
    public Message sendGroupMessage(String senderId, String groupId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setGroupId(groupId);
        message.setContent(content);
        message.setType("text");
        message.setStatus("sent");
        message.setSentAt(new Date());
        return messageRepository.save(message);
    }

    // 分页查询私聊消息
    public Page<Message> getPrivateMessages(String senderId, String receiverId, Pageable pageable) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageable);
    }

    // 分页查询群聊消息
    public Page<Message> getGroupMessages(String groupId, Pageable pageable) {
        return messageRepository.findByGroupId(groupId, pageable);
    }

    // 撤回消息
    @Transactional
    public void recallMessage(String messageId) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setStatus("recalled");
            message.setUpdatedAt(new Date());
            messageRepository.save(message);
        });
    }

    // 编辑消息
    @Transactional
    public void editMessage(String messageId, String newContent) {
        messageRepository.findById(messageId).ifPresent(message -> {
            message.setContent(newContent);
            message.setUpdatedAt(new Date());
            messageRepository.save(message);
        });
    }
}