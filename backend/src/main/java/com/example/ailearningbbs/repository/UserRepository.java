package com.example.ailearningbbs.repository;

import com.example.ailearningbbs.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRepository {
    User findById(@Param("id") Long id);
    User findByUsername(@Param("username") String username);
    User findByEmail(@Param("email") String email);
    int insert(User user);
    int update(User user);
    int deleteById(@Param("id") Long id);

    Integer countFollowers(@Param("userId") Long userId);
    Integer countFollowing(@Param("userId") Long userId);
    Integer countPosts(@Param("userId") Long userId);
}