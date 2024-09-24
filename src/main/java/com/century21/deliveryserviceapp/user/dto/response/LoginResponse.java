package com.century21.deliveryserviceapp.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;

    public static LoginResponse of(String accessToken) {
        return new LoginResponse(accessToken);
    }
}
