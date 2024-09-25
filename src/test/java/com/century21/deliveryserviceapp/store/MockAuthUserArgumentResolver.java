package com.century21.deliveryserviceapp.store;

import com.century21.deliveryserviceapp.data.AuthUserMockUtil;
import com.century21.deliveryserviceapp.data.UserMockDataUtil;
import com.century21.deliveryserviceapp.user.auth.AuthUserArgumentResolver;
import jakarta.annotation.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MockAuthUserArgumentResolver extends AuthUserArgumentResolver {

    @Override
    public Object resolveArgument(
            @Nullable MethodParameter parameter,
            @Nullable ModelAndViewContainer modelAndViewContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
            ){
        return AuthUserMockUtil.userAuth();
    }
}
