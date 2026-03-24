package com.example.ailearningbbs.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private Long recipientId;
    private Long actorId;
    private String type;
    private String targetType;
    private Long targetId;
    private String data;
    private Boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
