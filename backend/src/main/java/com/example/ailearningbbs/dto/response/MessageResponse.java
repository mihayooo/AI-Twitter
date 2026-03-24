package com.example.ailearningbbs.dto.response;

import com.example.ailearningbbs.entity.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private String senderAvatar;
    private Long receiverId;
    private String receiverUsername;
    private String receiverAvatar;
    private String content;
    private Boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static MessageResponse from(Message message, String senderUsername, String senderAvatar, String receiverUsername, String receiverAvatar) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setSenderId(message.getSenderId());
        response.setSenderUsername(senderUsername);
        response.setSenderAvatar(senderAvatar);
        response.setReceiverId(message.getReceiverId());
        response.setReceiverUsername(receiverUsername);
        response.setReceiverAvatar(receiverAvatar);
        response.setContent(message.getContent());
        response.setIsRead(message.getIsRead());
        response.setCreatedAt(message.getCreatedAt());
        return response;
    }
}
