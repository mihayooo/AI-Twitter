package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.Comment;
import com.example.ailearningbbs.entity.Post;
import com.example.ailearningbbs.repository.CommentRepository;
import com.example.ailearningbbs.repository.PostRepository;
import com.example.ailearningbbs.repository.UserRepository;
import com.example.ailearningbbs.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public Comment findById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> findByPostId(Long postId, int page, int size) {
        return commentRepository.findByPostId(postId, page, size);
    }

    public int countByPostId(Long postId) {
        Integer count = commentRepository.countByPostId(postId);
        return count != null ? count : 0;
    }

    public List<Comment> findByParentId(Long parentId, int page, int size) {
        return commentRepository.findByParentId(parentId, page, size);
    }

    public int countByParentId(Long parentId) {
        Integer count = commentRepository.countByParentId(parentId);
        return count != null ? count : 0;
    }

    @Transactional
    public Comment create(Long postId, Long userId, String content, Long parentId) {
        // 检查帖子是否存在
        Post post = postRepository.findById(postId);
        if (post == null) {
            throw new RuntimeException("帖子不存在");
        }

        // 如果有父评论，检查父评论是否存在
        if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId);
            if (parentComment == null || !parentComment.getPostId().equals(postId)) {
                throw new RuntimeException("父评论不存在");
            }
        }

        Comment comment = new Comment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());

        commentRepository.insert(comment);

        // 更新帖子评论数
        postRepository.incrementCommentCount(postId);

        // 填充用户信息
        var user = userRepository.findById(userId);
        if (user != null) {
            comment.setUsername(user.getUsername());
            comment.setUserAvatar(user.getAvatar());
        }

        // 发送评论通知（不给自己发）
        if (!post.getUserId().equals(userId)) {
            notificationService.createCommentNotification(postId, userId);
        }

        return comment;
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = findById(commentId);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("无权限删除此评论");
        }

        commentRepository.deleteById(commentId);
        postRepository.decrementCommentCount(comment.getPostId());
    }
}