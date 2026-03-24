package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.Message;
import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.repository.MessageRepository;
import com.example.ailearningbbs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public List<Message> getConversation(Long userId, Long otherUserId, int page, int size) {
        return messageRepository.findConversations(userId, otherUserId, page, size);
    }

    public List<Map<String, Object>> getUserList(Long userId) {
        return messageRepository.findUserList(userId);
    }

    public long countUnread(Long receiverId, Long senderId) {
        return messageRepository.countUnread(receiverId, senderId);
    }

    @Transactional
    public Message send(Long senderId, Long receiverId, String content) {
        if (senderId.equals(receiverId)) {
            throw new RuntimeException("不能给自己发送消息");
        }

        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setIsRead(false);
        message.setCreatedAt(LocalDateTime.now());

        messageRepository.insert(message);
        return message;
    }

    @Transactional
    public void markAsRead(Long receiverId, Long senderId) {
        messageRepository.markAsRead(receiverId, senderId);
    }

    @Transactional
    public void markAllAsRead(Long receiverId, Long senderId) {
        messageRepository.markAllAsRead(receiverId, senderId);
    }
}
