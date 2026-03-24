package com.example.ailearningbbs.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String content;
    private String resourceUrl;
    private String tags;
    private Integer likeCount;
    private Integer commentCount;
    private String status;
    private String reviewNote;
    private Long reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联属性（不映射到数据库）
    private String username;
    private String userAvatar;
    private Boolean liked;
    private Boolean bookmarked;
}