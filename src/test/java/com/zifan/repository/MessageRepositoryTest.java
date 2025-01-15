package com.zifan.repository;


import com.zifan.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void TestSave() {
        Message message = new Message();
        message.setSenderId("lizifan@qq.com");
        message.setReceiverId("lizifan2@qq.com");
        message.setContent("this is a message");
        message.setType("text");
        message.setStatus("sent");
        message.setSentAt(new Date());
        Message savedmessage = messageRepository.save(message);
        System.out.println(savedmessage);

    }
}
