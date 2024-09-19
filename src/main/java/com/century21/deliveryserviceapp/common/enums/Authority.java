package com.century21.deliveryserviceapp.common.enums;

import com.century21.deliveryserviceapp.common.exception.ApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.century21.deliveryserviceapp.common.exception.ResponseCode.INVALID_USER_AUTHORITY;

@Getter
@RequiredArgsConstructor
public enum Authority {
    OWNER("사장님"),
    USER("사용자");

    private final String authorityName;

    public static Authority getAuthority(String authorityName) {
        for (Authority authority : Authority.values()) {
            if (authority.getAuthorityName().equals(authorityName)) {
                return authority;
            }
        }

        throw ApiException.of(INVALID_USER_AUTHORITY);
    }
}
