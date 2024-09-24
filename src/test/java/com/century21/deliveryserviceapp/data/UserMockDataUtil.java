package com.century21.deliveryserviceapp.data;

import com.century21.deliveryserviceapp.common.config.PasswordEncoder;
import com.century21.deliveryserviceapp.entity.User;
import com.century21.deliveryserviceapp.user.dto.request.SignUpRequest;
import org.springframework.test.util.ReflectionTestUtils;

public class UserMockDataUtil {
    public static SignUpRequest signUpRequest() {
        return new SignUpRequest("email", "password", "nickname", "사장님");
    }

    public static SignUpRequest signUpRequestWithOwner(String email) {
        return new SignUpRequest(email, "password", "nickname", "사장님");
    }

    public static SignUpRequest signUpRequestWithUser() {
        return new SignUpRequest("email3", "password", "nickname", "사용자");
    }

    public static User user() {
        User user = User.from(signUpRequest(), new PasswordEncoder());
        ReflectionTestUtils.setField(user, "id", 1L);

        return user;
    }
}
