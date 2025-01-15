package com.zifan.dto.utils;

import com.zifan.dto.response.FriendshipResponse;
import com.zifan.dto.response.LoginResponse;
import com.zifan.model.Friendship;
import com.zifan.model.User;

import java.util.List;

public class DtoConverter {

    public static LoginResponse ConvertUser2LoginResponse(User user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setFirstName(user.getFirstName());
        loginResponse.setLastName(user.getLastName());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setStatus(user.getStatus());
        loginResponse.setRole(user.getRole());
        loginResponse.setCreatedAt(user.getCreatedAt());
        loginResponse.setUpdatedAt(user.getUpdatedAt());
        loginResponse.setLastLoginAt(user.getLastLoginAt());
        loginResponse.setAvatarUrl(user.getAvatarUrl());
        loginResponse.setBio(user.getBio());
        return loginResponse;
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
