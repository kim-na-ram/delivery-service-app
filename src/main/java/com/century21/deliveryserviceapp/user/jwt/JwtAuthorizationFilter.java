package com.century21.deliveryserviceapp.user.jwt;

import com.century21.deliveryserviceapp.user.auth.AuthUser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();

        // 회원가입 및 로그인 경로는 필터를 적용하지 않음
        if ("/api/users/signup".equals(uri) || "/api/users/login".equals(uri)) {
            chain.doFilter(request, response);
            return; // 필터 건너뛰기
        }

        // 토큰 검증
        String token = jwtUtil.resolveToken(httpRequest.getHeader(JwtUtil.AUTH_ACCESS_HEADER));
        if (token == null || !jwtUtil.validateToken(token)) {
            log.warn("유효하지 않은 JWT 토큰입니다.");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // JWT가 유효한 경우 필터 체인 계속 진행
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필요시 초기화 작업 수행
    }

    @Override
    public void destroy() {
        // 필요시 자원 해제 작업 수행
    }
}