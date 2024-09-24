package com.century21.deliveryserviceapp.user.jwt;

import com.century21.deliveryserviceapp.common.enums.TokenStatus;
import com.century21.deliveryserviceapp.entity.Token;
import com.century21.deliveryserviceapp.token.service.TokenService;
import com.century21.deliveryserviceapp.user.auth.AuthUser;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.century21.deliveryserviceapp.common.constant.Constant.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final TokenService tokenService;

    // 환경변수 또는 설정 파일에서 시크릿 키를 가져오기
    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // Token 생성
    public String createToken(Long userId, String authority, long expire) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expire);

        log.info("Creating access token for user: {}", userId);
        String result = Jwts.builder()
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
    public TokenStatus validateToken(String accessToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken);
            return TokenStatus.USUAL;
        } catch (SecurityException | MalformedJwtException | io.jsonwebtoken.security.SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        } catch (ExpiredJwtException e) {
            if (tokenService.isExistRefreshToken(accessToken)) {
                return TokenStatus.ACCESS_TOKEN_EXPIRE;
            }
        }
        return TokenStatus.UNUSUAL;
    }

    public String republishToken(String accessToken) {
        Token token = tokenService.findByAccessToken(accessToken);

        // refreshToken 을 통해 userId를 꺼내옴
        AuthUser authUser = getAuthUserFromToken(token.getRefreshToken());

        // jwtUtil 에서 새로운 토큰을 발행
        String newAccessToken = createToken(authUser.getUserId(), authUser.getAuthority(), ACCESS_TOKEN_EXPIRE_TIME);
        String newRefreshToken = createToken(authUser.getUserId(), authUser.getAuthority(), REFRESH_TOKEN_EXPIRE_TIME);

        tokenService.republishToken(token, newAccessToken, newRefreshToken);
        return newAccessToken;
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