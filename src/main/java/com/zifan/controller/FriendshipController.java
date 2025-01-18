package com.zifan.controller;

import com.zifan.dto.response.ApiResponse;
import com.zifan.dto.response.FriendshipResponse;
import com.zifan.service.FriendshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friendships")
public class FriendshipController {

    private static final Logger logger = LoggerFactory.getLogger(FriendshipController.class);

    @Autowired
    private FriendshipService friendshipService;

    // 发送好友请求（WebSocket）
    @MessageMapping("/friendships.sendFriendRequest")
    public ApiResponse<FriendshipResponse> sendFriendRequest(
            @Header("userEmail") String userEmail,
            @Header("friendEmail") String friendEmail) {
        try {
            logger.info("WebSocket: Sending friend request from {} to {}", userEmail, friendEmail);
            FriendshipResponse response = friendshipService.sendFriendRequest(userEmail, friendEmail);
            return ApiResponse.success(response, "Friend request sent successfully");
        } catch (Exception e) {
            logger.error("WebSocket: Failed to send friend request from {} to {}", userEmail, friendEmail, e);
            return ApiResponse.error("Failed to send friend request: " + e.getMessage());
        }
    }

    // 发送好友请求（HTTP）
    @PostMapping("/send-request")
    public ApiResponse<FriendshipResponse> sendFriendRequestHttp(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("friendEmail") String friendEmail) {
        try {
            logger.info("HTTP: Sending friend request from {} to {}", userEmail, friendEmail);
            FriendshipResponse response = friendshipService.sendFriendRequest(userEmail, friendEmail);
            return ApiResponse.success(response, "Friend request sent successfully");
        } catch (Exception e) {
            logger.error("HTTP: Failed to send friend request from {} to {}", userEmail, friendEmail, e);
            return ApiResponse.error("Failed to send friend request: " + e.getMessage());
        }
    }

    // 接受好友请求（WebSocket）
    @MessageMapping("/friendships.acceptFriendRequest")
    public ApiResponse<FriendshipResponse> acceptFriendRequest(
            @Header("userEmail") String userEmail,
            @Header("friendEmail") String friendEmail) {
        try {
            logger.info("WebSocket: Accepting friend request from {} by {}", friendEmail, userEmail);
            FriendshipResponse response = friendshipService.acceptFriendRequest(userEmail, friendEmail);
            return ApiResponse.success(response, "Friend request accepted successfully");
        } catch (Exception e) {
            logger.error("WebSocket: Failed to accept friend request from {} by {}", friendEmail, userEmail, e);
            return ApiResponse.error("Failed to accept friend request: " + e.getMessage());
        }
    }

    // 接受好友请求（HTTP）
    @PostMapping("/accept-request")
    public ApiResponse<FriendshipResponse> acceptFriendRequestHttp(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("friendEmail") String friendEmail) {
        try {
            logger.info("HTTP: Accepting friend request from {} by {}", friendEmail, userEmail);
            FriendshipResponse response = friendshipService.acceptFriendRequest(userEmail, friendEmail);
            return ApiResponse.success(response, "Friend request accepted successfully");
        } catch (Exception e) {
            logger.error("HTTP: Failed to accept friend request from {} by {}", friendEmail, userEmail, e);
            return ApiResponse.error("Failed to accept friend request: " + e.getMessage());
        }
    }

    // 拒绝好友请求（WebSocket）
    @MessageMapping("/friendships.rejectFriendRequest")
    public ApiResponse<FriendshipResponse> rejectFriendRequest(
            @Header("userEmail") String userEmail,
            @Header("friendEmail") String friendEmail) {
        try {
            logger.info("WebSocket: Rejecting friend request from {} by {}", friendEmail, userEmail);
            FriendshipResponse response = friendshipService.rejectFriendRequest(userEmail, friendEmail);
            return ApiResponse.success(response, "Friend request rejected successfully");
        } catch (Exception e) {
            logger.error("WebSocket: Failed to reject friend request from {} by {}", friendEmail, userEmail, e);
            return ApiResponse.error("Failed to reject friend request: " + e.getMessage());
        }
    }

    // 拒绝好友请求（HTTP）
    @PostMapping("/reject-request")
    public ApiResponse<FriendshipResponse> rejectFriendRequestHttp(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("friendEmail") String friendEmail) {
        try {
            logger.info("HTTP: Rejecting friend request from {} by {}", friendEmail, userEmail);
            FriendshipResponse response = friendshipService.rejectFriendRequest(userEmail, friendEmail);
            return ApiResponse.success(response, "Friend request rejected successfully");
        } catch (Exception e) {
            logger.error("HTTP: Failed to reject friend request from {} by {}", friendEmail, userEmail, e);
            return ApiResponse.error("Failed to reject friend request: " + e.getMessage());
        }
    }

    // 获取用户的好友列表（HTTP）
    @GetMapping("/friends")
    public ApiResponse<FriendshipResponse> getFriends(@RequestParam("userEmail") String userEmail) {
        try {
            logger.info("HTTP: Fetching friends for user {}", userEmail);
            FriendshipResponse response = friendshipService.getFriends(userEmail);
            return ApiResponse.success(response, "Friends list fetched successfully");
        } catch (Exception e) {
            logger.error("HTTP: Failed to fetch friends for user {}", userEmail, e);
            return ApiResponse.error("Failed to fetch friends: " + e.getMessage());
        }
    }

    // 获取用户的好友请求（HTTP）
    @GetMapping("/requests")
    public ApiResponse<FriendshipResponse> getFriendRequests(@RequestParam("userEmail") String userEmail) {
        try {
            logger.info("HTTP: Fetching friend requests for user {}", userEmail);
            FriendshipResponse response = friendshipService.getFriendRequests(userEmail);
            return ApiResponse.success(response, "Friend requests fetched successfully");
        } catch (Exception e) {
            logger.error("HTTP: Failed to fetch friend requests for user {}", userEmail, e);
            return ApiResponse.error("Failed to fetch friend requests: " + e.getMessage());
        }
    }

    // WebSocket: 获取用户的好友列表
    @MessageMapping("/friendships.getFriends")
    public ApiResponse<FriendshipResponse> getFriendsWebSocket(@Header("userEmail") String userEmail) {
        try {
            logger.info("WebSocket: Fetching friends for user {}", userEmail);
            FriendshipResponse response = friendshipService.getFriends(userEmail);
            return ApiResponse.success(response, "Friends list fetched successfully");
        } catch (Exception e) {
            logger.error("WebSocket: Failed to fetch friends for user {}", userEmail, e);
            return ApiResponse.error("Failed to fetch friends: " + e.getMessage());
        }
    }

    // WebSocket: 获取用户的好友请求
    @MessageMapping("/friendships.getFriendRequests")
    public ApiResponse<FriendshipResponse> getFriendRequestsWebSocket(@Header("userEmail") String userEmail) {
        try {
            logger.info("WebSocket: Fetching friend requests for user {}", userEmail);
            FriendshipResponse response = friendshipService.getFriendRequests(userEmail);
            return ApiResponse.success(response, "Friend requests fetched successfully");
        } catch (Exception e) {
            logger.error("WebSocket: Failed to fetch friend requests for user {}", userEmail, e);
            return ApiResponse.error("Failed to fetch friend requests: " + e.getMessage());
        }
    }
}