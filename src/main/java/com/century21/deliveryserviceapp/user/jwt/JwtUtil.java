package com.century21.deliveryserviceapp.user.jwt;

import com.century21.deliveryserviceapp.user.auth.AuthUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // 사용자 권한 키
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTH_ACCESS_HEADER = "Authorization";
    private final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L; // 60분

    // 환경변수 또는 설정 파일에서 시크릿 키를 가져오기
    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;


    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String email, String authority) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        log.info("Creating access token for user: {}", email);
        String result = Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("authority", authority)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
        return result;
    }

    // JWT에서 사용자 정보 추출
    public AuthUser getAuthUserFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long userId = claims.get("userId", Long.class);
        String authority = claims.get("authority", String.class);

        log.info("userId: {}, authority: {}", userId, authority);
        return new AuthUser(userId, authority);
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // HTTP 요청에서 JWT 토큰 추출
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        log.warn("인가 헤더가 없거나 BEARER가 없습니다");
        return null;
    }

}