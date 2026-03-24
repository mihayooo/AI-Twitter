package com.example.ailearningbbs.controller;

import com.example.ailearningbbs.dto.request.UpdateProfileRequest;
import com.example.ailearningbbs.dto.response.ApiResponse;
import com.example.ailearningbbs.dto.response.UserResponse;
import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.security.UserPrincipal;
import com.example.ailearningbbs.service.FollowService;
import com.example.ailearningbbs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        User user = userService.findById(id);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        UserResponse response = userService.toUserResponse(user);

        if (userPrincipal != null) {
            response.setIsFollowing(followService.isFollowing(userPrincipal.getId(), id));
        }

        return ApiResponse.success(response);
    }

    @PutMapping("/profile")
    public ApiResponse<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        User user = userService.updateProfile(userPrincipal.getId(), request);
        return ApiResponse.success(userService.toUserResponse(user));
    }

    @PostMapping("/{id}/follow")
    public ApiResponse<?> toggleFollow(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        try {
            boolean following = followService.toggleFollow(userPrincipal.getId(), id);
            return ApiResponse.success(following);
        } catch (RuntimeException e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @GetMapping("/{id}/followers")
    public ApiResponse<List<UserResponse>> getFollowers(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        int offset = (page - 1) * size;
        List<Long> followerIds = followService.getFollowerIds(id);

        List<UserResponse> followers = followerIds.stream()
                .skip(offset)
                .limit(size)
                .map(userId -> {
                    User user = userService.findById(userId);
                    return userService.toUserResponse(user);
                })
                .collect(Collectors.toList());

        return ApiResponse.success(followers);
    }

    @GetMapping("/{id}/following")
    public ApiResponse<List<UserResponse>> getFollowing(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        int offset = (page - 1) * size;
        List<Long> followingIds = followService.getFollowingIds(id);

        List<UserResponse> following = followingIds.stream()
                .skip(offset)
                .limit(size)
                .map(userId -> {
                    User user = userService.findById(userId);
                    return userService.toUserResponse(user);
                })
                .collect(Collectors.toList());

        return ApiResponse.success(following);
    }
}