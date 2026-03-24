package com.example.ailearningbbs.controller;

import com.example.ailearningbbs.dto.request.LoginRequest;
import com.example.ailearningbbs.dto.request.RegisterRequest;
import com.example.ailearningbbs.dto.response.ApiResponse;
import com.example.ailearningbbs.dto.response.LoginResponse;
import com.example.ailearningbbs.dto.response.UserResponse;
import com.example.ailearningbbs.entity.User;
import com.example.ailearningbbs.security.JwtTokenProvider;
import com.example.ailearningbbs.security.UserPrincipal;
import com.example.ailearningbbs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ApiResponse<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId());

        LoginResponse response = LoginResponse.of(
                token,
                jwtTokenProvider.getExpiration(),
                userService.toUserResponse(user)
        );

        return ApiResponse.success(response);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user;

        // 支持用户名或邮箱登录
        if (request.getLogin().contains("@")) {
            user = userService.findByEmail(request.getLogin());
        } else {
            user = userService.findByUsername(request.getLogin());
        }

        if (user == null) {
            return ApiResponse.error(401, "用户名/邮箱或密码错误");
        }

        if (!userService.verifyPassword(request.getPassword(), user.getPasswordHash())) {
            return ApiResponse.error(401, "用户名/邮箱或密码错误");
        }

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId());

        LoginResponse response = LoginResponse.of(
                token,
                jwtTokenProvider.getExpiration(),
                userService.toUserResponse(user)
        );

        return ApiResponse.success(response);
    }

    @GetMapping("/profile")
    public ApiResponse<UserResponse> getProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ApiResponse.error(401, "未登录");
        }

        User user = userService.findById(userPrincipal.getId());
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }

        return ApiResponse.success(userService.toUserResponse(user));
    }
}