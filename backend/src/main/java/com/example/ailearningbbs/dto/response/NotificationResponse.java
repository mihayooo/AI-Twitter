package com.example.ailearningbbs.dto.response;

import com.example.ailearningbbs.entity.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long actorId;
    private String actorUsername;
    private String actorAvatar;
    private String type;
    private String targetType;
    private Long targetId;
    private Boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static NotificationResponse from(Notification notification, String actorUsername, String actorAvatar) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setActorId(notification.getActorId());
        response.setActorUsername(actorUsername);
        response.setActorAvatar(actorAvatar);
        response.setType(notification.getType());
        response.setTargetType(notification.getTargetType());
        response.setTargetId(notification.getTargetId());
        response.setIsRead(notification.getIsRead());
        response.setCreatedAt(notification.getCreatedAt());
        return response;
    }
}
