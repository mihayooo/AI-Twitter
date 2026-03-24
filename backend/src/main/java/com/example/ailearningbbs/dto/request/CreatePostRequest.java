package com.example.ailearningbbs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostRequest {
    @NotBlank(message = "帖子内容不能为空")
    @Size(min = 1, max = 5000, message = "帖子内容长度不能超过5000个字符")
    private String content;

    private String resourceUrl;

    private String tags;
}