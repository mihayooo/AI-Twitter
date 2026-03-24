package com.example.ailearningbbs.controller;

import com.example.ailearningbbs.dto.response.ApiResponse;
import com.example.ailearningbbs.dto.response.MessageResponse;
import com.example.ailearningbbs.entity.Message;
import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.repository.UserRepository;
import com.example.ailearningbbs.security.UserPrincipal;
import com.example.ailearningbbs.service.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @PostMapping
    public ApiResponse<MessageResponse> sendMessage(
            @RequestParam Long receiverId,
            @Valid @RequestBody SendMessageRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        Message message = messageService.send(userPrincipal.getId(), receiverId, request.getContent());

        User sender = userRepository.findById(userPrincipal.getId());
        User receiver = userRepository.findById(receiverId);

        return ApiResponse.success(MessageResponse.from(
                message,
                sender != null ? sender.getUsername() : null,
                sender != null ? sender.getAvatar() : null,
                receiver != null ? receiver.getUsername() : null,
                receiver != null ? receiver.getAvatar() : null
        ));
    }

    @GetMapping("/conversation/{otherUserId}")
    public ApiResponse<List<MessageResponse>> getConversation(
            @PathVariable Long otherUserId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        List<Message> messages = messageService.getConversation(userPrincipal.getId(), otherUserId, page, size);

        List<MessageResponse> responses = messages.stream()
                .map(message -> {
                    User sender = userRepository.findById(message.getSenderId());
                    User receiver = userRepository.findById(message.getReceiverId());
                    return MessageResponse.from(
                            message,
                            sender != null ? sender.getUsername() : null,
                            sender != null ? sender.getAvatar() : null,
                            receiver != null ? receiver.getUsername() : null,
                            receiver != null ? receiver.getAvatar() : null
                    );
                })
                .collect(Collectors.toList());

        return ApiResponse.success(responses);
    }

    @GetMapping("/users")
    public ApiResponse<List<Map<String, Object>>> getUserList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        List<Map<String, Object>> userList = messageService.getUserList(userPrincipal.getId());
        return ApiResponse.success(userList);
    }

    @PutMapping("/read/{senderId}")
    public ApiResponse<?> markAsRead(
            @PathVariable Long senderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        messageService.markAsRead(userPrincipal.getId(), senderId);
        return ApiResponse.success();
    }

    @PutMapping("/read/all/{senderId}")
    public ApiResponse<?> markAllAsRead(
            @PathVariable Long senderId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ApiResponse.error(401, "需要登录");
        }

        messageService.markAllAsRead(userPrincipal.getId(), senderId);
        return ApiResponse.success();
    }

    public static class SendMessageRequest {
        @NotBlank(message = "消息内容不能为空")
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
