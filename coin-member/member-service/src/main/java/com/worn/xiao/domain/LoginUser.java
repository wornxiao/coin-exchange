package com.worn.xiao.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser {
    private String username;
    private Long expiresIn;
    private String accessToken;
    private String refreshToken;
}
