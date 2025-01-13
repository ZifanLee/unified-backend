package com.zifan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "messages")
@Data
public class Message {

    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private String groupId;
    private String content;
    private String type; // text, image, file
    private String status; // sent, delivered, read
    private Date sentAt;
    private Date updatedAt; // 用于消息编辑时间

}
