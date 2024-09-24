package com.century21.deliveryserviceapp.common.config.filter;

import com.century21.deliveryserviceapp.common.enums.TokenStatus;
import com.century21.deliveryserviceapp.common.response.ErrorResponse;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import com.century21.deliveryserviceapp.user.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.century21.deliveryserviceapp.common.constant.Constant.AUTH_USER;
import static com.century21.deliveryserviceapp.common.constant.Constant.BEARER_PREFIX;
import static com.century21.deliveryserviceapp.common.exception.ResponseCode.INVALID_JWT_TOKEN;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final List<String[]> whiteList = List.of(
            new String[]{"/api/users/login", "POST"},
            new String[]{"/api/users/signup", "POST"},
            new String[]{"/api/stores", "GET"},
            new String[]{"/api/reviews/", "GET"}
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 특정 uri 와 http method 는 필터를 건너뛴다
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (!checkUrlPathAndMethodIsStartWiths(uri, method, whiteList)) {
            // 그 외 요청에 대해서는 JWT 필터를 적용
            String authHeader = request.getHeader(AUTHORIZATION);

            if (Strings.isBlank(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                handleErrorResponse(response, HttpStatus.FORBIDDEN, INVALID_JWT_TOKEN.getMessage());
                return;
            }

            String token = jwtUtil.resolveToken(authHeader);
            TokenStatus tokenStatus = jwtUtil.validateToken(token);

            if (tokenStatus.equals(TokenStatus.UNUSUAL)) {
                handleErrorResponse(response, HttpStatus.FORBIDDEN, INVALID_JWT_TOKEN.getMessage());
                return;
            }

            if (tokenStatus.equals(TokenStatus.ACCESS_TOKEN_EXPIRE)) {
                token = jwtUtil.republishToken(token);
            }

            request.setAttribute(AUTHORIZATION, token);

            AuthUser authUser = jwtUtil.getAuthUserFromToken(token);
            request.setAttribute(AUTH_USER, authUser);
        }

        filterChain.doFilter(request, response);
    }

    public boolean checkUrlPathAndMethodIsStartWiths(String url, String method, List<String[]> urlList) {
        return urlList.stream().anyMatch(i -> url.startsWith(i[0]) && method.equalsIgnoreCase(i[1]));
    }

    private void handleErrorResponse(HttpServletResponse response, HttpStatus httpStatus, String message) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus.value(), message);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonToString = objectMapper.writeValueAsString(errorResponse);

        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setStatus(httpStatus.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonToString);
        log.error("[{}] {}", httpStatus, message);
    }
}