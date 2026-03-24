package com.example.ailearningbbs.repository;

import com.example.ailearningbbs.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationRepository {
    int insert(Notification notification);
    List<Notification> findByRecipientId(@Param("recipientId") Long recipientId, @Param("page") int page, @Param("size") int size);
    List<Notification> findUnreadByRecipientId(@Param("recipientId") Long recipientId, @Param("page") int page, @Param("size") int size);
    long countUnread(@Param("recipientId") Long recipientId);
    Notification findById(@Param("id") Long id);
    int markAsRead(@Param("id") Long id, @Param("recipientId") Long recipientId);
    int markAllAsRead(@Param("recipientId") Long recipientId);
    int deleteById(@Param("id") Long id, @Param("recipientId") Long recipientId);
    int deleteByRecipientId(@Param("recipientId") Long recipientId);
}
