package com.example.ailearningbbs.repository;

import com.example.ailearningbbs.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CommentRepository {
    Comment findById(@Param("id") Long id);
    List<Comment> findByPostId(@Param("postId") Long postId, @Param("page") int page, @Param("size") int size);
    List<Comment> findByParentId(@Param("parentId") Long parentId, @Param("page") int page, @Param("size") int size);
    int insert(Comment comment);
    int deleteById(@Param("id") Long id);
    int countByPostId(@Param("postId") Long postId);
    int countByParentId(@Param("parentId") Long parentId);
}