package com.century21.deliveryserviceapp.user.jwt;

import com.century21.deliveryserviceapp.user.dto.request.LoginRequest;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;



@Slf4j
public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 로그인 및 회원가입 요청은 필터를 건너뛴다
        String uri = httpRequest.getRequestURI();
        if ("/api/users/login".equals(uri) || "/api/users/signup".equals(uri)) {
            chain.doFilter(request, response);  // 필터 건너뜀
            return;
        }

        // 그 외 요청에 대해서는 JWT 필터를 적용
        String authHeader = httpRequest.getHeader(AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(JwtUtil.BEARER_PREFIX)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("{\"status\": 401, \"message\": \"Authorization 헤더가 없습니다.\"}");
            return;
        }

        String token = authHeader.substring(JwtUtil.BEARER_PREFIX.length());
        if (!jwtUtil.validateToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.getWriter().write("{\"status\": 403, \"message\": \"유효하지 않은 토큰입니다.\"}");
            return;
        }

        // 토큰이 유효하면 필터 체인 계속 진행
        chain.doFilter(request, response);
    }

    private void handleForbiddenResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("{\"status\": 403, \"message\": \"" + message + "\"}");
        log.warn("Forbidden - {}", message);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
