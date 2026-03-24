package com.example.ailearningbbs.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private String bio;
    private LocalDateTime createdAt;
    private Integer followerCount;
    private Integer followingCount;
    private Integer postCount;
    private Boolean isFollowing;
}