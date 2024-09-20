package com.century21.deliveryserviceapp.user.jwt;

import com.century21.deliveryserviceapp.user.dto.request.LoginRequest;
import com.century21.deliveryserviceapp.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.AuthenticationException;
import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/users/login");  // 로그인 요청 URL 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 요청에서 로그인 데이터를 파싱하여 읽음
            LoginRequest loginRequestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            // UsernamePasswordAuthenticationToken 생성 (인증 토큰 생성)
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

            // 인증 매니저에게 전달하여 인증 시도
            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            log.error("로그인 요청 처리 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("로그인 요청 처리 중 오류 발생", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        // 로그인 성공 시 사용자 이메일 가져오기
        String email = authResult.getName();

        // Access Token과 Refresh Token 생성
        String accessToken = jwtUtil.createToken(email);


        // 응답 헤더에 Access Token과 Refresh Token 설정
        response.addHeader(JwtUtil.AUTH_ACCESS_HEADER, JwtUtil.BEARER_PREFIX + accessToken);

        // 성공 응답 전송 (JSON 형식으로 Access Token 반환)
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\": 200, \"message\": \"로그인 성공\", \"access_token\": \"" + accessToken + "\"}");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        // 로그인 실패 시 처리
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\": 401, \"message\": \"로그인 실패: 잘못된 이메일 또는 비밀번호입니다.\"}");
    }
}