package com.century21.deliveryserviceapp.user.auth;

import com.century21.deliveryserviceapp.common.annotaion.Auth;
import com.century21.deliveryserviceapp.user.jwt.JwtUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.century21.deliveryserviceapp.common.constant.Constant.AUTH_USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 컨트롤러 메서드 파라미터에 Auth 어노테이션과 AuthUser 타입이 있을 경우 처리
        return parameter.getParameterAnnotation(Auth.class) != null
                && parameter.getParameterType().equals(AuthUser.class);
    }


    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) {


        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        AuthUser authUser = (AuthUser) request.getAttribute(AUTH_USER);
        return authUser;
    }
}