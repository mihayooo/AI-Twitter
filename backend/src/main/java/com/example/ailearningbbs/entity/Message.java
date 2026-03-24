package com.example.ailearningbbs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // 关联属性
    private String senderUsername;
    private String senderAvatar;
    private String receiverUsername;
    private String receiverAvatar;
}
