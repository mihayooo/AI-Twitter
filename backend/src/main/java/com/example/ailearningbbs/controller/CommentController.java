package com.example.ailearningbbs.controller;

import com.example.ailearningbbs.dto.request.CreateCommentRequest;
import com.example.ailearningbbs.dto.response.ApiResponse;
import com.example.ailearningbbs.dto.response.CommentResponse;
import com.example.ailearningbbs.entity.Comment;
import com.example.ailearningbbs.security.UserPrincipal;
import com.example.ailearningbbs.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/post/{postId}")
    public ApiResponse<List<CommentResponse>> getCommentsByPostId(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        int offset = (page - 1) * size;
        List<Comment> comments = commentService.findByPostId(postId, offset, size);

        List<CommentResponse> responses = comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @GetMapping("/{commentId}/replies")
    public ApiResponse<List<CommentResponse>> getReplies(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        int offset = (page - 1) * size;
        List<Comment> replies = commentService.findByParentId(commentId, offset, size);

        List<CommentResponse> responses = replies.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @PostMapping("/post/{postId}")
    public ApiResponse<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        Comment comment = commentService.create(
                postId,
                userPrincipal.getId(),
                request.getContent(),
                request.getParentId()
        );

        return ApiResponse.success(CommentResponse.from(comment));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        commentService.delete(commentId, userPrincipal.getId());
        return ApiResponse.success();
    }

    @GetMapping("/{commentId}")
    public ApiResponse<CommentResponse> getComment(
            @PathVariable Long commentId) {

        Comment comment = commentService.findById(commentId);
        if (comment == null) {
            return ApiResponse.error(404, "评论不存在");
        }

        return ApiResponse.success(CommentResponse.from(comment));
    }
}
