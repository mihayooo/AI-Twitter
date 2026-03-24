package com.example.ailearningbbs.controller;

import com.example.ailearningbbs.dto.response.ApiResponse;
import com.example.ailearningbbs.dto.response.NotificationResponse;
import com.example.ailearningbbs.entity.Notification;
import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.repository.UserRepository;
import com.example.ailearningbbs.security.UserPrincipal;
import com.example.ailearningbbs.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        int offset = (page - 1) * size;
        List<Notification> notifications = notificationService.findNotifications(userPrincipal.getId(), offset, size);

        List<NotificationResponse> responses = notifications.stream()
                .map(notification -> {
                    User actor = userRepository.findById(notification.getActorId());
                    return NotificationResponse.from(
                            notification,
                            actor != null ? actor.getUsername() : "Unknown",
                            actor != null ? actor.getAvatar() : null
                    );
                })
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @GetMapping("/unread/count")
    public ApiResponse<Long> getUnreadCount(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        long count = notificationService.countUnread(userPrincipal.getId());
        return ApiResponse.success(count);
    }

    @GetMapping("/unread")
    public ApiResponse<List<NotificationResponse>> getUnreadNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        int offset = (page - 1) * size;
        List<Notification> notifications = notificationService.findUnreadNotifications(userPrincipal.getId(), offset, size);

        List<NotificationResponse> responses = notifications.stream()
                .map(notification -> {
                    User actor = userRepository.findById(notification.getActorId());
                    return NotificationResponse.from(
                            notification,
                            actor != null ? actor.getUsername() : "Unknown",
                            actor != null ? actor.getAvatar() : null
                    );
                })
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @PutMapping("/{notificationId}/read")
    public ApiResponse<?> markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        notificationService.markAsRead(notificationId, userPrincipal.getId());
        return ApiResponse.success();
    }

    @PutMapping("/read/all")
    public ApiResponse<?> markAllAsRead(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        notificationService.markAllAsRead(userPrincipal.getId());
        return ApiResponse.success();
    }

    @DeleteMapping("/{notificationId}")
    public ApiResponse<?> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        notificationService.deleteNotification(notificationId, userPrincipal.getId());
        return ApiResponse.success();
    }
}
