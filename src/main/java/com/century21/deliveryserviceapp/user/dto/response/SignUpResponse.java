package com.century21.deliveryserviceapp.user.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponse {
    private Long userId;
    private String email;
    private String nickname;

    @NotNull
    private String authority;
}
