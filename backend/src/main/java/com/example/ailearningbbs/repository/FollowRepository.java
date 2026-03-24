package com.example.ailearningbbs.repository;

import com.example.ailearningbbs.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FollowRepository {
    Follow findByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    int insert(Follow follow);
    int deleteByFollowerIdAndFollowingId(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);
    List<Long> findFollowerIdsByFollowingId(@Param("followingId") Long followingId);
    int countFollowing(@Param("followerId") Long followerId);
    int countFollowers(@Param("followingId") Long followingId);
}