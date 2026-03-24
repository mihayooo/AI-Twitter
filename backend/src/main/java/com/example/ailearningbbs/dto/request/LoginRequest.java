package com.example.ailearningbbs.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "用户名或邮箱不能为空")
    private String login;

    @NotBlank(message = "密码不能为空")
    private String password;
}