package com.century21.deliveryserviceapp.user.auth;

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

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 컨트롤러 메서드 파라미터에 AuthUser 타입이 있는 경우 처리
        return parameter.getParameterType().equals(AuthUser.class);
    }


    @Override
    public Object resolveArgument(@Nullable MethodParameter parameter,
                                  @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  @Nullable WebDataBinderFactory binderFactory) {


        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        // Authorization 헤더에서 토큰 추출
        String token = jwtUtil.resolveToken(request.getHeader(JwtUtil.AUTH_ACCESS_HEADER));

        if (token != null && jwtUtil.validateToken(token)) {
            log.info("JWT 토큰이 유효합니다. AuthUser 생성.");
            // 토큰에서 AuthUser 객체 추출
            return jwtUtil.getAuthUserFromToken(token);
        }

        log.warn("유효하지 않은 JWT 토큰입니다.");
        return null;  // 유효하지 않은 경우 null 반환
    }
}