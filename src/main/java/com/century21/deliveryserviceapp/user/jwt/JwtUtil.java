package com.century21.deliveryserviceapp.user.jwt;

import com.century21.deliveryserviceapp.common.enums.Authority;
import com.century21.deliveryserviceapp.common.exception.ApiException;
import com.century21.deliveryserviceapp.common.exception.ResponseCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.error.ShouldContainOnly;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtUtil {
    // accessToken 토큰 헤더
    public static final String AUTH_ACCESS_HEADER = "Authorization";
    // refreshToken 토큰 헤더
    public static final String AUTH_REFRESH_HEADER = "RefreshToken";
    // 사용자 권한 키
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // accessToken 만료 시간 (60분)
    private final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;
    // refreshToken 만료 시간 (2주)
    private final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 24 * 60 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JWT 토큰 생성
    public String createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT 토큰에서 사용자 이메일 추출
    public String getUserEmail(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw ApiException.of(ResponseCode.EXPIRED_JWT_TOKEN);
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            throw ApiException.of(ResponseCode.INVALID_JWT_TOKEN);
        }
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw ApiException.of(ResponseCode.EXPIRED_JWT_TOKEN);  // JWT 토큰 만료
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            throw ApiException.of(ResponseCode.INVALID_JWT_TOKEN);  // 유효하지 않은 JWT 토큰
        }
    }

    // HTTP 요청에서 JWT 토큰 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getAccessTokenFromHeader(HttpServletRequest request){
        String accessToken = request.getHeader(AUTH_ACCESS_HEADER);
        if(StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER_PREFIX)) {
            return accessToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}