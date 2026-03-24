package com.example.ailearningbbs.repository;

import com.example.ailearningbbs.entity.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LikeRepository {
    Like findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
    int insert(Like like);
    int deleteByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
    int countByPostId(@Param("postId") Long postId);
}