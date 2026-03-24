package com.example.ailearningbbs.repository;

import com.example.ailearningbbs.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageRepository {
    int insert(Message message);
    List<Message> findBySenderIdAndReceiverId(
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId,
            @Param("page") int page,
            @Param("size") int size);
    List<Message> findConversations(
            @Param("userId") Long userId,
            @Param("otherUserId") Long otherUserId,
            @Param("page") int page,
            @Param("size") int size);
    List<java.util.Map<String, Object>> findUserList(@Param("userId") Long userId);
    long countUnread(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);
    int markAsRead(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);
    int markAllAsRead(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);
}
