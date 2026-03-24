package com.example.ailearningbbs.controller;

import com.example.ailearningbbs.dto.response.ApiResponse;
import com.example.ailearningbbs.dto.response.PostResponse;
import com.example.ailearningbbs.entity.Post;
import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.repository.UserRepository;
import com.example.ailearningbbs.security.UserPrincipal;
import com.example.ailearningbbs.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PostService postService;
    private final UserRepository userRepository;

    private boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId);
        return user != null && "admin".equals(user.getRole());
    }

    @GetMapping("/posts/pending")
    public ApiResponse<List<PostResponse>> getPendingPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        if (!isAdmin(userPrincipal.getId())) {
            return ApiResponse.error(403, "需要管理员权限");
        }

        int offset = (page - 1) * size;
        List<Post> posts = postService.findPendingPosts(offset, size);

        List<PostResponse> responses = posts.stream()
                .map(post -> postService.toPostResponse(post, userPrincipal.getId()))
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @PutMapping("/posts/{postId}/review")
    public ApiResponse<PostResponse> reviewPost(
            @PathVariable Long postId,
            @Valid @RequestBody ReviewRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        if (!isAdmin(userPrincipal.getId())) {
            return ApiResponse.error(403, "需要管理员权限");
        }

        Post post = postService.reviewPost(postId, userPrincipal.getId(), request.getStatus(), request.getReviewNote());
        PostResponse response = postService.toPostResponse(post, userPrincipal.getId());

        return ApiResponse.success(response);
    }

    public static class ReviewRequest {
        private String status; // approved, rejected
        private String reviewNote;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReviewNote() {
            return reviewNote;
        }

        public void setReviewNote(String reviewNote) {
            this.reviewNote = reviewNote;
        }
    }
}
