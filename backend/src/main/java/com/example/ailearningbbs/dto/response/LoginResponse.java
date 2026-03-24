package com.example.ailearningbbs.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private Long expiresIn;
    private UserResponse user;

    public static LoginResponse of(String token, Long expiresIn, UserResponse user) {
        return new LoginResponse(token, "Bearer", expiresIn, user);
    }
}