package com.century21.deliveryserviceapp.user.dto.request;

import lombok.Getter;

@Getter
public class SignUpRequest {
    private String email;
    private String password;
    private String nickname;
    private String authority;

    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
