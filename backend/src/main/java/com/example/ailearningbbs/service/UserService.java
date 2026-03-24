package com.example.ailearningbbs.service;

import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.repository.UserRepository;
import com.example.ailearningbbs.dto.request.RegisterRequest;
import com.example.ailearningbbs.dto.request.UpdateProfileRequest;
import com.example.ailearningbbs.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User register(RegisterRequest request) {
        // 检查用户名是否存在
        if (findByUsername(request.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否存在
        if (findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAvatar("/default-avatar.png");
        user.setBio("");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.insert(user);
        return user;
    }

    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Transactional
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 如果要修改用户名，检查是否已被占用
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (findByUsername(request.getUsername()) != null) {
                throw new RuntimeException("用户名已存在");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.update(user);
        return user;
    }

    public UserResponse toUserResponse(User user) {
        return toUserResponse(user, null);
    }

    public UserResponse toUserResponse(User user, Long currentUserId) {
        if (user == null) return null;

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setAvatar(user.getAvatar());
        response.setBio(user.getBio());
        response.setCreatedAt(user.getCreatedAt());

        // 统计数量
        Integer followerCount = userRepository.countFollowers(user.getId());
        Integer followingCount = userRepository.countFollowing(user.getId());
        Integer postCount = userRepository.countPosts(user.getId());

        response.setFollowerCount(followerCount != null ? followerCount : 0);
        response.setFollowingCount(followingCount != null ? followingCount : 0);
        response.setPostCount(postCount != null ? postCount : 0);

        return response;
    }
}