package com.zifan.repository;


import com.zifan.model.UserSignal;
import com.zifan.model.enumType.SignalType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class SignalRepositoryTest {

    @Autowired
    private SignalRepository signalRepository;

    @Test
    void testSave() {
        String module = "message";
        String userEmail = "lizifan@qq.com";
        UserSignal userSignal = signalRepository.findByUserEmail(userEmail)
                .orElse(new UserSignal(userEmail));

        UserSignal.Signal signal = new UserSignal.Signal();
        signal.setTimestamp(LocalDateTime.now()); // 设置信号时间
        signal.setId("testId");
        signal.setType(SignalType.NEW_MESSAGE);
        boolean add = userSignal.getModules().computeIfAbsent(module, k -> List.of()).add(signal);
        UserSignal createdSignal = signalRepository.save(userSignal);   // 保存到 MongoDB
        System.out.println(createdSignal);
    }

    @Test
    void testfindByUserEmail() {
        Optional<UserSignal> userSignal = signalRepository.findByUserEmail("lizifan@qq.com");
        System.out.println(userSignal);
    }

    @Test
    void testfindByUserEmailAndModuleAndId() {
        Optional<UserSignal> userSignal = signalRepository.findByUserEmailAndModuleAndId("lizifan@qq.com", "message", "signal_id_01");
        System.out.println(userSignal);
    }

    @Test
    void testfindByUserEmailAndModule() {
        Optional<UserSignal> userSignal = signalRepository.findByUserEmailAndModule("lizifan@qq.com", "message");
        System.out.println(userSignal);
    }

    @Test
    void testdeleteByUserEmailAndModuleAndId() {
        signalRepository.deleteByUserEmailAndModuleAndId("lizifan@qq.com", "message", "testId");
        Optional<UserSignal> userSignal = signalRepository.findByUserEmail("lizifan@qq.com");
        System.out.println(userSignal);
    }

    @Test
    void testdeleteModuleByUserEmail() {
        signalRepository.deleteModuleByUserEmail("lizifan@qq.com", "message");
        Optional<UserSignal> userSignal = signalRepository.findByUserEmail("lizifan@qq.com");
        System.out.println(userSignal);
    }

}
