package com.example.ailearningbbs.repository;

import com.example.ailearningbbs.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostRepository {
    Post findById(@Param("id") Long id);
    List<Post> findAllByStatus(@Param("status") String status, @Param("page") int page, @Param("size") int size);
    List<Post> findByUserId(@Param("userId") Long userId, @Param("page") int page, @Param("size") int size);
    List<Post> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status, @Param("page") int page, @Param("size") int size);
    List<Post> findByTag(@Param("tag") String tag, @Param("page") int page, @Param("size") int size);
    List<Post> findByTagAndStatus(@Param("tag") String tag, @Param("status") String status, @Param("page") int page, @Param("size") int size);
    List<Post> findByUserIds(@Param("userIds") List<Long> userIds, @Param("page") int page, @Param("size") int size);
    List<Post> findByUserIdsAndStatus(@Param("userIds") List<Long> userIds, @Param("status") String status, @Param("page") int page, @Param("size") int size);
    List<java.util.Map<String, Object>> findHotTags(@Param("limit") int limit);
    List<Post> findByStatus(@Param("status") String status, @Param("page") int page, @Param("size") int size);
    int insert(Post post);
    int update(Post post);
    int deleteById(@Param("id") Long id);
    int incrementLikeCount(@Param("id") Long id);
    int decrementLikeCount(@Param("id") Long id);
    int incrementCommentCount(@Param("id") Long id);
    int decrementCommentCount(@Param("id") Long id);
    int count();
}