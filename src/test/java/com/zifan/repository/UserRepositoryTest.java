package com.zifan.repository;


import com.zifan.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void TestfindByUsername() {
        String username = "Tang Jialun";
        List<User> users = userRepository.findByUsername(username);
        // 判空
        if (users == null || users.isEmpty()) {
            System.out.println("No users found with username: " + username);
            return;
        }

        // 遍历列表并打印用户信息
        for (User user : users) {
            System.out.println("User found: " + user);
        }
    }
}
