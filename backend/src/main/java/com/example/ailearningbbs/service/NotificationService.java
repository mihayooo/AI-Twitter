package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.Notification;
import com.example.ailearningbbs.entity.Post;
import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.repository.CommentRepository;
import com.example.ailearningbbs.repository.FollowRepository;
import com.example.ailearningbbs.repository.NotificationRepository;
import com.example.ailearningbbs.repository.PostRepository;
import com.example.ailearningbbs.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // Notification types
    public static final String TYPE_LIKE = "like";
    public static final String TYPE_COMMENT = "comment";
    public static final String TYPE_FOLLOW = "follow";

    public List<Notification> findNotifications(Long userId, int page, int size) {
        return notificationRepository.findByRecipientId(userId, page, size);
    }

    public List<Notification> findUnreadNotifications(Long userId, int page, int size) {
        return notificationRepository.findUnreadByRecipientId(userId, page, size);
    }

    public long countUnread(Long userId) {
        return notificationRepository.countUnread(userId);
    }

    public Notification findById(Long id, Long userId) {
        Notification notification = notificationRepository.findById(id);
        if (notification == null || !notification.getRecipientId().equals(userId)) {
            return null;
        }
        return notification;
    }

    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        notificationRepository.markAsRead(notificationId, userId);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        notificationRepository.deleteById(notificationId, userId);
    }

    @Transactional
    public void createLikeNotification(Long postId, Long likerId) {
        Post post = postRepository.findById(postId);
        if (post == null || post.getUserId().equals(likerId)) {
            return; // 作者不能收到自己点赞的通知
        }

        Notification notification = new Notification();
        notification.setRecipientId(post.getUserId());
        notification.setActorId(likerId);
        notification.setType(TYPE_LIKE);
        notification.setTargetType("post");
        notification.setTargetId(postId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.insert(notification);
    }

    @Transactional
    public void createCommentNotification(Long postId, Long commenterId) {
        Post post = postRepository.findById(postId);
        if (post == null || post.getUserId().equals(commenterId)) {
            return; // 作者不能收到自己评论的通知
        }

        Notification notification = new Notification();
        notification.setRecipientId(post.getUserId());
        notification.setActorId(commenterId);
        notification.setType(TYPE_COMMENT);
        notification.setTargetType("post");
        notification.setTargetId(postId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.insert(notification);
    }

    @Transactional
    public void createFollowNotification(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            return; // 不能关注自己
        }

        Notification notification = new Notification();
        notification.setRecipientId(followingId);
        notification.setActorId(followerId);
        notification.setType(TYPE_FOLLOW);
        notification.setTargetType("user");
        notification.setTargetId(followerId);
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.insert(notification);
    }
}
