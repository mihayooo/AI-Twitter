package com.example.ailearningbbs.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private Long postId;
    private Long userId;
    private Long parentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联属性
    private String username;
    private String userAvatar;
}