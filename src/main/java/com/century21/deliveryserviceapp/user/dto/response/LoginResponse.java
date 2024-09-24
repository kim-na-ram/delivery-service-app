package com.century21.deliveryserviceapp.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String refreshToken;  // 필요에 따라 리프레시 토큰도 추가할 수 있습니다.

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
