package com.zifan.controller;

import com.zifan.dto.response.FriendshipResponse;
import com.zifan.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    // 发送好友请求（WebSocket）
    @MessageMapping("/friendships.sendFriendRequest")
    public FriendshipResponse sendFriendRequest(
            @Header("userEmail") String userEmail,
            @Header("friendEmail") String friendEmail) {
        return friendshipService.sendFriendRequest(userEmail, friendEmail);
    }

    // 发送好友请求（HTTP）
    @PostMapping("/send-request")
    public FriendshipResponse sendFriendRequestHttp(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userEmail") String friendEmail) {
        return friendshipService.sendFriendRequest(userEmail, friendEmail);
    }

    // 接受好友请求（WebSocket）
    @MessageMapping("/friendships.acceptFriendRequest")
    public FriendshipResponse acceptFriendRequest(
            @Header("userEmail") String userEmail,
            @Header("friendEmail") String friendEmail) {
        return friendshipService.acceptFriendRequest(userEmail, friendEmail);
    }

    // 接受好友请求（HTTP）
    @PostMapping("/accept-request")
    public FriendshipResponse acceptFriendRequestHttp(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("friendEmail") String friendEmail) {
        return friendshipService.acceptFriendRequest(userEmail, friendEmail);
    }

    // 拒绝好友请求（WebSocket）
    @MessageMapping("/friendships.rejectFriendRequest")
    public FriendshipResponse rejectFriendRequest(
            @Header("userEmail") String userEmail,
            @Header("friendEmail") String friendEmail) {
        return friendshipService.rejectFriendRequest(userEmail, friendEmail);
    }

    // 拒绝好友请求（HTTP）
    @PostMapping("/reject-request")
    public FriendshipResponse rejectFriendRequestHttp(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("friendEmail") String friendEmail) {
        return friendshipService.rejectFriendRequest(userEmail, friendEmail);
    }

    // 获取用户的好友列表（HTTP）
    @GetMapping("/friends")
    public FriendshipResponse getFriends(@RequestParam("userEmail") String userEmail) {
        return friendshipService.getFriends(userEmail);
    }

    // 获取用户的好友请求（HTTP）
    @GetMapping("/requests")
    public FriendshipResponse getFriendRequests(@RequestParam("userEmail") String userEmail) {
        return friendshipService.getFriendRequests(userEmail);
    }
}