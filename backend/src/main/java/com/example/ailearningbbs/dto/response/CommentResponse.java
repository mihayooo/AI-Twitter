package com.example.ailearningbbs.dto.response;

import com.example.ailearningbbs.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private String userAvatar;
    private Long parentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer replyCount; // 回复数量

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setPostId(comment.getPostId());
        response.setUserId(comment.getUserId());
        response.setUsername(comment.getUsername());
        response.setUserAvatar(comment.getUserAvatar());
        response.setParentId(comment.getParentId());
        response.setContent(comment.getContent());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());
        return response;
    }
}
