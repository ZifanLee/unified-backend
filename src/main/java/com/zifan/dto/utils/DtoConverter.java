package com.zifan.dto.utils;

import com.zifan.dto.response.FriendshipResponse;
import com.zifan.dto.response.LoginResponse;
import com.zifan.dto.response.RegisterResponse;
import com.zifan.model.Friendship;
import com.zifan.model.User;

import java.util.List;

public class DtoConverter {

    public static RegisterResponse ConvertUser2RegisterResponse(User user) {
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(user.getId());
        registerResponse.setUsername(user.getUsername());
        registerResponse.setEmail(user.getEmail());
        registerResponse.setFirstName(user.getFirstName());
        registerResponse.setLastName(user.getLastName());
        registerResponse.setPhone(user.getPhone());
        registerResponse.setStatus(user.getStatus());
        registerResponse.setRole(user.getRole());
        registerResponse.setCreatedAt(user.getCreatedAt());
        registerResponse.setUpdatedAt(user.getUpdatedAt());
        registerResponse.setLastLoginAt(user.getLastLoginAt());
        registerResponse.setAvatarUrl(user.getAvatarUrl());
        registerResponse.setBio(user.getBio());
        return registerResponse;
    }

    public static FriendshipResponse ConvertFrinedship2FriendshipResponse(Friendship friendship) {
        FriendshipResponse friendshipResponse = new FriendshipResponse();
        friendshipResponse.setId(friendship.getId().toString());
        friendshipResponse.setUserEmail(friendship.getUserEmail());
        friendshipResponse.setFriendEmail(friendship.getFriendEmail());
        friendshipResponse.setCreatedAt( friendship.getCreatedAt());
        friendshipResponse.setUpdatedAt(friendship.getUpdatedAt());
        friendshipResponse.setStatus(friendship.getStatus().name());
        return friendshipResponse;
    }

    public static FriendshipResponse ConvertFriendshipList2FriendResponse(String userEmail, List<Friendship> friendships) {
        FriendshipResponse friendshipResponse = new FriendshipResponse();
        friendshipResponse.setUserEmail(userEmail);
        friendshipResponse.setFriendships(friendships);
        return friendshipResponse;
    }
}
