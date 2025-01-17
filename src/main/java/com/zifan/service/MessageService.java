package com.zifan.service;

import com.zifan.exception.validation.IllegalUserEmailException;
import com.zifan.model.Message;
import com.zifan.repository.MessageRepository;
import com.zifan.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import static com.zifan.service.utils.ValidationUtils.validateEmail;

@Service
public class MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageNotificationService messageNotificationService;

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private SignalService signalService;

    // 发送私聊消息
    @Transactional
    public Message sendPrivateMessage(String senderId, String receiverId, String content) {
        logger.info("发送私聊消息: 发送者={}, 接收者={}, 内容={}", senderId, receiverId, content);

        try {
            // 校验接收者邮箱
            validateEmail(receiverId);
            logger.debug("接收者邮箱校验通过: {}", receiverId);

            // 校验发送者邮箱是否合法
            if (!JwtUtil.AuthenticateEmail(senderId)) {
                logger.error("发送者邮箱非法: {}", senderId);
                throw new IllegalUserEmailException();
            }

            // 创建消息对象
            Message message = createMessage(senderId, content);
            message.setReceiverId(receiverId);

            // 存储消息
            message = saveMessage(message);
            logger.info("消息存储成功: 消息ID={}", message.getId());

            // 判断接收者是否在线
            if (userStatusService.isUserOnline(receiverId)) {
                logger.info("接收者在线: {}", receiverId);
                // 通过 RabbitMQ 发送消息通知给接收者
                messageNotificationService.notifyPrivateMessage(receiverId, message);
                logger.info("消息已通过 RabbitMQ 发送给接收者: {}", receiverId);
            } else {
                logger.info("接收者离线: {}", receiverId);
                // 存储到离线信号系统
                signalService.addNewMessageSignal(receiverId, message.getId());
                logger.info("消息已存储到离线信号系统: 接收者={}, 消息ID={}", receiverId, message.getId());
            }

            return message;
        } catch (Exception e) {
            logger.error("发送私聊消息失败: 发送者={}, 接收者={}, 错误信息={}", senderId, receiverId, e.getMessage(), e);
            throw new RuntimeException("发送私聊消息失败", e);
        }
    }

    // 发送群聊消息
    // todo 关于群聊消息的在线判定，合法性检查等还需要斟酌，理论上应该通过mq发送，同时存储离线
    @Transactional
    public Message sendGroupMessage(String senderId, String groupId, String content) {
        logger.info("发送群聊消息: 发送者={}, 群组ID={}, 内容={}", senderId, groupId, content);

        try {
            // 创建消息对象
            Message message = createMessage(senderId, content);
            message.setGroupId(groupId);

            // 存储消息
            message = saveMessage(message);
            logger.info("消息存储成功: 消息ID={}", message.getId());

            // 通过 RabbitMQ 发送消息通知给群组
            messageNotificationService.notifyGroupMessage(groupId, message);
            logger.info("消息已通过 RabbitMQ 发送给群组: 群组ID={}", groupId);

            return message;
        } catch (Exception e) {
            logger.error("发送群聊消息失败: 发送者={}, 群组ID={}, 错误信息={}", senderId, groupId, e.getMessage(), e);
            throw new RuntimeException("发送群聊消息失败", e);
        }
    }

    // 分页查询私聊消息
    public Page<Message> getPrivateMessages(String senderId, String receiverId, Pageable pageable) {
        logger.info("查询私聊消息: 发送者={}, 接收者={}", senderId, receiverId);

        try {
            Page<Message> messages = messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageable);
            logger.info("查询私聊消息成功: 发送者={}, 接收者={}, 消息数量={}", senderId, receiverId, messages.getTotalElements());
            return messages;
        } catch (Exception e) {
            logger.error("查询私聊消息失败: 发送者={}, 接收者={}, 错误信息={}", senderId, receiverId, e.getMessage(), e);
            throw new RuntimeException("查询私聊消息失败", e);
        }
    }

    // 分页查询群聊消息
    public Page<Message> getGroupMessages(String groupId, Pageable pageable) {
        logger.info("查询群聊消息: 群组ID={}", groupId);

        try {
            Page<Message> messages = messageRepository.findByGroupId(groupId, pageable);
            logger.info("查询群聊消息成功: 群组ID={}, 消息数量={}", groupId, messages.getTotalElements());
            return messages;
        } catch (Exception e) {
            logger.error("查询群聊消息失败: 群组ID={}, 错误信息={}", groupId, e.getMessage(), e);
            throw new RuntimeException("查询群聊消息失败", e);
        }
    }

    // 撤回消息
    @Transactional
    public void recallMessage(String messageId) {
        logger.info("撤回消息: 消息ID={}", messageId);

        try {
            updateMessage(messageId, message -> {
                message.setStatus("recalled");
                message.setUpdatedAt(new Date());
            });
            logger.info("消息撤回成功: 消息ID={}", messageId);
        } catch (Exception e) {
            logger.error("撤回消息失败: 消息ID={}, 错误信息={}", messageId, e.getMessage(), e);
            throw new RuntimeException("撤回消息失败", e);
        }
    }

    // 编辑消息
    @Transactional
    public void editMessage(String messageId, String newContent) {
        logger.info("编辑消息: 消息ID={}, 新内容={}", messageId, newContent);

        try {
            updateMessage(messageId, message -> {
                message.setContent(newContent);
                message.setUpdatedAt(new Date());
            });
            logger.info("消息编辑成功: 消息ID={}", messageId);
        } catch (Exception e) {
            logger.error("编辑消息失败: 消息ID={}, 错误信息={}", messageId, e.getMessage(), e);
            throw new RuntimeException("编辑消息失败", e);
        }
    }

    // 创建消息对象
    private Message createMessage(String senderId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setContent(content);
        message.setType("text");
        message.setStatus("sent");
        message.setSentAt(new Date());
        return message;
    }

    // 保存消息
    private Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    // 更新消息
    // todo 有待考察，暂未支持，这个功能支持需要前后端联调
    private void updateMessage(String messageId, MessageUpdater updater) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        optionalMessage.ifPresent(message -> {
            updater.update(message);
            saveMessage(message);

            // 通过 RabbitMQ 通知消息更新
            if (message.getReceiverId() != null) {
                messageNotificationService.notifyPrivateMessage(message.getReceiverId(), message);
                logger.info("消息更新通知已发送给接收者: 接收者={}, 消息ID={}", message.getReceiverId(), message.getId());
            } else if (message.getGroupId() != null) {
                messageNotificationService.notifyGroupMessage(message.getGroupId(), message);
                logger.info("消息更新通知已发送给群组: 群组ID={}, 消息ID={}", message.getGroupId(), message.getId());
            }
        });
    }

    // 消息更新接口
    @FunctionalInterface
    private interface MessageUpdater {
        void update(Message message);
    }
}