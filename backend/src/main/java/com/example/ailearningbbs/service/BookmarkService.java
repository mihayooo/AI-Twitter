package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.Bookmark;
import com.example.ailearningbbs.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;

    public boolean isBookmarked(Long userId, Long postId) {
        return bookmarkRepository.findByUserIdAndPostId(userId, postId) != null;
    }

    @Transactional
    public boolean toggleBookmark(Long userId, Long postId) {
        Bookmark existingBookmark = bookmarkRepository.findByUserIdAndPostId(userId, postId);

        if (existingBookmark != null) {
            // 已经收藏，取消收藏
            bookmarkRepository.deleteByUserIdAndPostId(userId, postId);
            return false;
        } else {
            // 未收藏，添加收藏
            Bookmark bookmark = new Bookmark();
            bookmark.setUserId(userId);
            bookmark.setPostId(postId);
            bookmarkRepository.insert(bookmark);
            return true;
        }
    }
}