package com.example.ailearningbbs.repository;

import com.example.ailearningbbs.entity.Bookmark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookmarkRepository {
    Bookmark findByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
    int insert(Bookmark bookmark);
    int deleteByUserIdAndPostId(@Param("userId") Long userId, @Param("postId") Long postId);
}