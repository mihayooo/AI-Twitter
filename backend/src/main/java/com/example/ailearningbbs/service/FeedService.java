package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.Post;
import com.example.ailearningbbs.repository.PostRepository;
import com.example.ailearningbbs.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final PostService postService;

    /**
     * 获取首页时间线（关注用户的帖子 + 全局热门）
     */
    public List<Post> getTimeline(Long userId, int page, int size) {
        // 1. 获取用户关注的所有人
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(userId);

        List<Post> posts;
        if (followingIds != null && !followingIds.isEmpty()) {
            // 2. 如果有关注，获取关注用户的帖子 + 一些全局热门帖子
            followingIds.add(userId); // 也包含自己的帖子
            posts = postRepository.findByUserIds(followingIds, page, size);

            // 如果关注用户的帖子不够，获取更多全局帖子补充
            if (posts.size() < size) {
                List<Post> allPosts = postRepository.findAllByStatus("approved", page, size);
                for (Post allPost : allPosts) {
                    boolean alreadyAdded = false;
                    for (Post p : posts) {
                        if (p.getId().equals(allPost.getId())) {
                            alreadyAdded = true;
                            break;
                        }
                    }
                    if (!alreadyAdded) {
                        posts.add(allPost);
                    }
                    if (posts.size() >= size) break;
                }
            }
        } else {
            // 3. 如果没有关注，返回全局帖子
            posts = postRepository.findAllByStatus("approved", page, size);
        }

        return posts;
    }

    /**
     * 获取发现页（全局帖子，按时间倒序）
     */
    public List<Post> getDiscover(int page, int size) {
        return postRepository.findAllByStatus("approved", page, size);
    }

    /**
     * 获取标签相关的帖子
     */
    public List<Post> getByTag(String tag, int page, int size) {
        return postRepository.findByTagAndStatus(tag, "approved", page, size);
    }
}