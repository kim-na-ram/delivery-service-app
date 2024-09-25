package com.century21.deliveryserviceapp.data;

import com.century21.deliveryserviceapp.user.auth.AuthUser;
import com.century21.deliveryserviceapp.common.enums.Authority;

public class AuthUserMockUtil {
    public static AuthUser ownerAuth() {
        return new AuthUser(1L, Authority.OWNER.name());
    }

    public static AuthUser userAuth() {
        return new AuthUser(1L, Authority.USER.name());
    }
}
