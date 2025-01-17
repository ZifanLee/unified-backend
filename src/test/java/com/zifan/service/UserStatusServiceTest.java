package com.zifan.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserStatusServiceTest {

    @Autowired
    private UserStatusService userStatusService;

    @Test
    void testAddUserStatus() {
        boolean ret = userStatusService.addUserStatus("lizifan@qq.com", "ONLINE");
        System.out.println(ret);

        ret = userStatusService.addUserStatus("lizifan@qq.com", "BUSY");
        System.out.println(ret);

        ret = userStatusService.addUserStatus("lizifan2@qq.com", "ONLINE");
        System.out.println(ret);

        ret = userStatusService.addUserStatus("lizifan@qq.com", "INVALID");
        System.out.println(ret);
    }

    @Test
    void testGetAllStatus() {
        List<String> userStatuses = userStatusService.getUserStatuses("lizifan@qq.com");
        System.out.println(userStatuses);
    }
}
