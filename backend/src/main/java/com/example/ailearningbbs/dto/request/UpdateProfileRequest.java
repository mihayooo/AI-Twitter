package com.example.ailearningbbs.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;

    @Size(max = 500, message = "个人简介不能超过500个字符")
    private String bio;

    private String avatar;
}