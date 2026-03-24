package com.example.ailearningbbs.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private Long userId;
    private String username;
    private String userAvatar;
    private String content;
    private String resourceUrl;
    private String tags;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private Boolean liked;
    private Boolean bookmarked;
    private String status;
    private String reviewNote;
}