package com.zifan.repository;

import com.zifan.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

    // 查询私聊消息
    List<Message> findBySenderIdAndReceiverIdOrderBySentAtAsc(String senderId, String receiverId);

    // 查询群聊消息
    List<Message> findByGroupIdOrderBySentAtAsc(String groupId);

    // 分页查询私聊消息
    Page<Message> findBySenderIdAndReceiverId(String senderId, String receiverId, Pageable pageable);

    // 分页查询群聊消息
    Page<Message> findByGroupId(String groupId, Pageable pageable);

    // 根据状态查询消息
    List<Message> findByStatus(String status);

    // 批量更新消息状态
    @Query("{ 'id': { $in: ?0 }, 'status': ?1 }")
    void updateMessagesStatus(List<String> messageIds, String status);
}