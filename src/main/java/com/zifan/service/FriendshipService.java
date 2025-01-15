package com.zifan.service;

import com.zifan.dto.response.FriendshipResponse;
import com.zifan.model.Friendship;
import com.zifan.repository.FriendshipRepository;
import com.zifan.dto.utils.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    // 发送好友请求
    @Transactional
    public FriendshipResponse sendFriendRequest(String userEmail, String friendEmail) {
        // 检查是否已经存在好友关系
        if (friendshipRepository.findByUserEmailAndFriendEmail(userEmail, friendEmail).isPresent()) {
            throw new IllegalArgumentException("好友关系已存在");
        }

        // 创建好友关系
        Friendship friendship = new Friendship();
        friendship.setUserEmail(userEmail);
        friendship.setFriendEmail(friendEmail);
        friendship.setStatus(Friendship.FriendshipStatus.PENDING);
        friendship.setCreatedAt(LocalDateTime.now());
        friendship.setUpdatedAt(LocalDateTime.now());

        return DtoConverter.ConvertFrinedship2FriendshipResponse(friendshipRepository.save(friendship));
    }

    // 接受好友请求
    @Transactional
    public FriendshipResponse acceptFriendRequest(String userEmail, String friendEmail) {
        Friendship friendship = friendshipRepository.findByUserEmailAndFriendEmail(friendEmail, userEmail)
                .orElseThrow(() -> new IllegalArgumentException("未找到好友请求"));

        if (friendship.getStatus() != Friendship.FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("好友请求状态不正确");
        }

        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        friendship.setUpdatedAt(LocalDateTime.now());

        return DtoConverter.ConvertFrinedship2FriendshipResponse(friendshipRepository.save(friendship));
    }

    // 拒绝好友请求
    @Transactional
    public FriendshipResponse rejectFriendRequest(String userEmail, String friendEmail) {
        Friendship friendship = friendshipRepository.findByUserEmailAndFriendEmail(friendEmail, userEmail)
                .orElseThrow(() -> new IllegalArgumentException("未找到好友请求"));

        if (friendship.getStatus() != Friendship.FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("好友请求状态不正确");
        }

        friendship.setStatus(Friendship.FriendshipStatus.REJECTED);
        friendship.setUpdatedAt(LocalDateTime.now());

        return DtoConverter.ConvertFrinedship2FriendshipResponse(friendshipRepository.save(friendship));
    }

    // 获取用户的好友列表
    public FriendshipResponse getFriends(String userEmail) {
        List<Friendship> friendships = friendshipRepository.findByUserEmailAndStatus(userEmail, Friendship.FriendshipStatus.ACCEPTED);
        return DtoConverter.ConvertFriendshipList2FriendResponse(userEmail, friendships);
    }

    // 获取用户的好友请求
    public FriendshipResponse getFriendRequests(String userEmail) {
        List<Friendship> friendships = friendshipRepository.findByFriendEmailAndStatus(userEmail, Friendship.FriendshipStatus.PENDING);
        return DtoConverter.ConvertFriendshipList2FriendResponse(userEmail, friendships);
    }
}