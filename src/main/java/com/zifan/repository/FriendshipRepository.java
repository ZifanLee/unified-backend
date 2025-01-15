package com.zifan.repository;

import com.zifan.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // 根据用户邮箱和好友邮箱查找好友关系
    Optional<Friendship> findByUserEmailAndFriendEmail(String userEmail, String friendEmail);

    // 查找用户的所有好友关系
    List<Friendship> findByUserEmail(String userEmail);

    // 查找用户的所有好友关系（根据状态过滤）
    List<Friendship> findByUserEmailAndStatus(String userEmail, Friendship.FriendshipStatus status);

    // 查找用户的好友请求（状态为 PENDING）
    List<Friendship> findByFriendEmailAndStatus(String friendEmail, Friendship.FriendshipStatus status);
}