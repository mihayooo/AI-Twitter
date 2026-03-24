package com.example.ailearningbbs.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Like {
    private Long id;
    private Long userId;
    private Long postId;
    private LocalDateTime createdAt;
}