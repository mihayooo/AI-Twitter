package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.Follow;
import com.example.ailearningbbs.repository.FollowRepository;
import com.example.ailearningbbs.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final NotificationService notificationService;

    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingId) != null;
    }

    @Transactional
    public boolean toggleFollow(Long followerId, Long followingId) {
        // 不能关注自己
        if (followerId.equals(followingId)) {
            throw new RuntimeException("不能关注自己");
        }

        Follow existingFollow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);

        if (existingFollow != null) {
            // 已经关注，取消关注
            followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
            return false;
        } else {
            // 未关注，添加关注
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowingId(followingId);
            followRepository.insert(follow);

            // 发送关注通知
            notificationService.createFollowNotification(followerId, followingId);

            return true;
        }
    }

    public int getFollowerCount(Long userId) {
        Integer count = followRepository.countFollowers(userId);
        return count != null ? count : 0;
    }

    public int getFollowingCount(Long userId) {
        Integer count = followRepository.countFollowing(userId);
        return count != null ? count : 0;
    }

    public List<Long> getFollowingIds(Long followerId) {
        return followRepository.findFollowingIdsByFollowerId(followerId);
    }

    public List<Long> getFollowerIds(Long followingId) {
        return followRepository.findFollowerIdsByFollowingId(followingId);
    }
}