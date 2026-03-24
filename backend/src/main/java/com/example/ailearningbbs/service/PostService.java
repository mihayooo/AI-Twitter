package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.Post;
import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.repository.PostRepository;
import com.example.ailearningbbs.dto.request.CreatePostRequest;
import com.example.ailearningbbs.dto.response.PostResponse;
import com.example.ailearningbbs.dto.response.TagStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAll(int page, int size) {
        return postRepository.findAllByStatus("approved", page, size);
    }

    public List<Post> findByUserId(Long userId, int page, int size) {
        return postRepository.findByUserIdAndStatus(userId, "approved", page, size);
    }

    public List<Post> findByTag(String tag, int page, int size) {
        return postRepository.findByTagAndStatus(tag, "approved", page, size);
    }

    public List<Post> findByUserIds(List<Long> userIds, int page, int size) {
        if (userIds == null || userIds.isEmpty()) {
            return new ArrayList<>();
        }
        return postRepository.findByUserIdsAndStatus(userIds, "approved", page, size);
    }

    public int count() {
        return postRepository.count();
    }

    public List<TagStats> findHotTags(int limit) {
        List<Map<String, Object>> results = postRepository.findHotTags(limit);
        List<TagStats> tagStats = new ArrayList<>();

        for (Map<String, Object> row : results) {
            String tag = (String) row.get("tag");
            Long count = (Long) row.get("tag_count");
            tagStats.add(new TagStats(tag, count.intValue()));
        }

        return tagStats;
    }

    @Transactional
    public Post create(Long userId, CreatePostRequest request) {
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(request.getContent());
        post.setResourceUrl(request.getResourceUrl());
        post.setTags(request.getTags() != null ? request.getTags() : "");
        post.setLikeCount(0);
        post.setCommentCount(0);
        post.setStatus("pending");
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.insert(post);
        return post;
    }

    @Transactional
    public Post update(Long postId, Long userId, CreatePostRequest request) {
        Post post = findById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权限修改此帖子");
        }

        post.setContent(request.getContent());
        if (request.getResourceUrl() != null) {
            post.setResourceUrl(request.getResourceUrl());
        }
        if (request.getTags() != null) {
            post.setTags(request.getTags());
        }
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.update(post);
        return post;
    }

    public List<TagStats> findHotTags(int limit) {
        List<Map<String, Object>> results = postRepository.findHotTags(limit);
        List<TagStats> tagStats = new ArrayList<>();

        for (Map<String, Object> row : results) {
            String tag = (String) row.get("tag");
            Long count = (Long) row.get("tag_count");
            tagStats.add(new TagStats(tag, count.intValue()));
        }

        return tagStats;
    }

    @Transactional
    public Post reviewPost(Long postId, Long reviewerId, String status, String reviewNote) {
        Post post = findById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        post.setStatus(status);
        if (reviewNote != null) {
            post.setReviewNote(reviewNote);
        }
        post.setReviewedBy(reviewerId);
        post.setReviewedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.update(post);
        return post;
    }

    public List<Post> findPendingPosts(int page, int size) {
        List<Post> posts = postRepository.findByStatus("pending", page, size);
        return posts;
    }

    @Transactional
    public void delete(Long postId, Long userId) {
        Post post = findById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        if (!post.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此帖子");
        }

        postRepository.deleteById(postId);
    }

    public PostResponse toPostResponse(Post post) {
        return toPostResponse(post, null);
    }

    public PostResponse toPostResponse(Post post, Long currentUserId) {
        if (post == null) return null;

        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setUserId(post.getUserId());
        response.setUsername(post.getUsername());
        response.setUserAvatar(post.getUserAvatar());
        response.setContent(post.getContent());
        response.setResourceUrl(post.getResourceUrl());
        response.setTags(post.getTags());
        response.setLikeCount(post.getLikeCount());
        response.setCommentCount(post.getCommentCount());
        response.setCreatedAt(post.getCreatedAt());
        response.setStatus(post.getStatus());
        response.setReviewNote(post.getReviewNote());

        return response;
    }
}