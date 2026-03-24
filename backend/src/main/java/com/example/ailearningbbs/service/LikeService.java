package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.Like;
import com.example.ailearningbbs.entity.Post;
import com.example.ailearningbbs.repository.LikeRepository;
import com.example.ailearningbbs.repository.PostRepository;
import com.example.ailearningbbs.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    public boolean isLiked(Long userId, Long postId) {
        return likeRepository.findByUserIdAndPostId(userId, postId) != null;
    }

    @Transactional
    public boolean toggleLike(Long userId, Long postId) {
        Like existingLike = likeRepository.findByUserIdAndPostId(userId, postId);

        if (existingLike != null) {
            // 已经点赞，取消点赞
            likeRepository.deleteByUserIdAndPostId(userId, postId);
            postRepository.decrementLikeCount(postId);
            return false;
        } else {
            // 未点赞，添加点赞
            Like like = new Like();
            like.setUserId(userId);
            like.setPostId(postId);
            likeRepository.insert(like);
            postRepository.incrementLikeCount(postId);

            // 发送点赞通知
            notificationService.createLikeNotification(postId, userId);

            return true;
        }
    }

    public int getLikeCount(Long postId) {
        Integer count = likeRepository.countByPostId(postId);
        return count != null ? count : 0;
    }
}