package com.century21.deliveryserviceapp.user.auth;

import com.century21.deliveryserviceapp.common.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUser {
    private Long userId;
    private String authority;  // 사용자의 권한 (예: USER, ADMIN 등)
}