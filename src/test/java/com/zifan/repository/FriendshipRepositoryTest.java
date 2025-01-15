package com.zifan.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.zifan.model.Friendship;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FriendshipRepositoryTest {

    @Autowired
    private FriendshipRepository friendshipRepository;

    private Friendship friendship;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        friendship = new Friendship();
        friendship.setUserEmail("userA@example.com");
        friendship.setFriendEmail("userB@example.com");
        friendship.setStatus(Friendship.FriendshipStatus.PENDING);
        friendship.setCreatedAt(LocalDateTime.now());
        friendship.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testSave() {
        friendship = new Friendship();
        friendship.setUserEmail("lizifan2@qq.com");
        friendship.setFriendEmail("lizifan@qq.com");
        friendship.setStatus(Friendship.FriendshipStatus.PENDING);
        friendship.setCreatedAt(LocalDateTime.now());
        friendship.setUpdatedAt(LocalDateTime.now());
        try {
            // 保存好友关系
            Friendship savedFriendship = friendshipRepository.save(friendship);
            System.out.println(savedFriendship);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    @Test
    void testSaveFriendship() {
        try {
            // 保存好友关系
            Friendship savedFriendship = friendshipRepository.save(friendship);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    @Test
    void testFindByUserEmailAndFriendEmail() {

        // 查找好友关系
        Optional<Friendship> foundFriendship = friendshipRepository.findByUserEmailAndFriendEmail(
                "userA@example.com",
                "userB@example.com"
        );

        // 验证结果
        assertTrue(foundFriendship.isPresent());
        assertEquals("userA@example.com", foundFriendship.get().getUserEmail());
        assertEquals("userB@example.com", foundFriendship.get().getFriendEmail());
    }

    @Test
    void testFindByUserEmail() {
        // 查找用户的所有好友关系
        List<Friendship> friendships = friendshipRepository.findByUserEmail("userA@example.com");

        // 验证结果
        assertEquals(1, friendships.size());
        assertEquals("userA@example.com", friendships.get(0).getUserEmail());
    }

    @Test
    void testFindByUserEmailAndStatus() {
        // 查找用户的 PENDING 状态的好友关系
        List<Friendship> friendships = friendshipRepository.findByUserEmailAndStatus(
                "userA@example.com",
                Friendship.FriendshipStatus.PENDING
        );

        // 验证结果
        assertEquals(1, friendships.size());
        assertEquals(Friendship.FriendshipStatus.PENDING, friendships.get(0).getStatus());
    }

    @Test
    void testFindByFriendEmailAndStatus() {
        // 查找用户的好友请求（状态为 PENDING）
        List<Friendship> friendships = friendshipRepository.findByFriendEmailAndStatus(
                "userB@example.com",
                Friendship.FriendshipStatus.PENDING
        );

        // 验证结果
        assertEquals(1, friendships.size());
        assertEquals(Friendship.FriendshipStatus.PENDING, friendships.get(0).getStatus());
    }
}
