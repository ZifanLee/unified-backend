package com.zifan.dto.response;

import com.zifan.model.Friendship;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FriendshipResponse {
    private String id;
    private String userEmail;
    private String friendEmail;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Friendship> friendships;
}