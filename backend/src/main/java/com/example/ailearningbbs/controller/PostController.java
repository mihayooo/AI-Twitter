package com.example.ailearningbbs.controller;

import com.example.ailearningbbs.dto.request.CreatePostRequest;
import com.example.ailearningbbs.dto.response.ApiResponse;
import com.example.ailearningbbs.dto.response.PostResponse;
import com.example.ailearningbbs.dto.response.TagStats;
import com.example.ailearningbbs.entity.Post;
import com.example.ailearningbbs.security.UserPrincipal;
import com.example.ailearningbbs.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final FeedService feedService;
    private final LikeService likeService;
    private final BookmarkService bookmarkService;
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping
    public ApiResponse<List<PostResponse>> getPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String tag,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        int offset = (page - 1) * size;
        List<Post> posts;

        if (tag != null && !tag.isEmpty()) {
            posts = postService.findByTag(tag, offset, size);
        } else {
            posts = postService.findAll(offset, size);
        }

        Long currentUserId = userPrincipal != null ? userPrincipal.getId() : null;
        List<PostResponse> responses = posts.stream()
                .map(post -> {
                    PostResponse response = postService.toPostResponse(post, currentUserId);
                    if (currentUserId != null) {
                        response.setLiked(likeService.isLiked(currentUserId, post.getId()));
                        response.setBookmarked(bookmarkService.isBookmarked(currentUserId, post.getId()));
                    }
                    return response;
                })
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @GetMapping("/timeline")
    public ApiResponse<List<PostResponse>> getTimeline(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        int offset = (page - 1) * size;
        List<Post> posts = feedService.getTimeline(userPrincipal.getId(), offset, size);

        Long currentUserId = userPrincipal.getId();
        List<PostResponse> responses = posts.stream()
                .map(post -> {
                    PostResponse response = postService.toPostResponse(post, currentUserId);
                    response.setLiked(likeService.isLiked(currentUserId, post.getId()));
                    response.setBookmarked(bookmarkService.isBookmarked(currentUserId, post.getId()));
                    return response;
                })
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<PostResponse>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        int offset = (page - 1) * size;
        List<Post> posts = postService.findByUserId(userId, offset, size);

        Long currentUserId = userPrincipal != null ? userPrincipal.getId() : null;
        List<PostResponse> responses = posts.stream()
                .map(post -> {
                    PostResponse response = postService.toPostResponse(post, currentUserId);
                    if (currentUserId != null) {
                        response.setLiked(likeService.isLiked(currentUserId, post.getId()));
                        response.setBookmarked(bookmarkService.isBookmarked(currentUserId, post.getId()));
                    }
                    return response;
                })
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @GetMapping("/{id}")
    public ApiResponse<PostResponse> getPost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Post post = postService.findById(id);
        if (post == null) {
            return ApiResponse.error(404, "帖子不存在");
        }

        // 检查帖子状态：只有作者、管理员或者已审核通过的帖子可见
        if (!"approved".equals(post.getStatus())) {
            if (userPrincipal == null || (!post.getUserId().equals(userPrincipal.getId()) && !isAdmin(userPrincipal.getId()))) {
                return ApiResponse.error(404, "帖子不存在或未审核通过");
            }
        }

        Long currentUserId = userPrincipal != null ? userPrincipal.getId() : null;
        PostResponse response = postService.toPostResponse(post, currentUserId);
        if (currentUserId != null) {
            response.setLiked(likeService.isLiked(currentUserId, id));
            response.setBookmarked(bookmarkService.isBookmarked(currentUserId, id));
        }

        return ApiResponse.success(response);
    }

    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        Post post = postService.create(userPrincipal.getId(), request);
        PostResponse response = postService.toPostResponse(post, userPrincipal.getId());

        return ApiResponse.success(response);
    }

    @PutMapping("/{id}")
    public ApiResponse<PostResponse> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        Post post = postService.update(id, userPrincipal.getId(), request);
        PostResponse response = postService.toPostResponse(post, userPrincipal.getId());

        return ApiResponse.success(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        postService.delete(id, userPrincipal.getId());
        return ApiResponse.success();
    }

    @PostMapping("/{id}/like")
    public ApiResponse<?> toggleLike(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        boolean liked = likeService.toggleLike(userPrincipal.getId(), id);
        return ApiResponse.success(liked);
    }

    @PostMapping("/{id}/bookmark")
    public ApiResponse<?> toggleBookmark(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        boolean bookmarked = bookmarkService.toggleBookmark(userPrincipal.getId(), id);
        return ApiResponse.success(bookmarked);
    }

    @GetMapping("/tags/hot")
    public ApiResponse<List<TagStats>> getHotTags(
            @RequestParam(defaultValue = "20") int limit) {

        List<TagStats> hotTags = postService.findHotTags(limit);
        return ApiResponse.success(hotTags);
    }

    private boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId);
        return user != null && "admin".equals(user.getRole());
    }
}