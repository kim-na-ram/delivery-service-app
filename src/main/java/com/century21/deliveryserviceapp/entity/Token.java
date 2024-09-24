package com.century21.deliveryserviceapp.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import static com.century21.deliveryserviceapp.common.constant.Constant.REFRESH_TOKEN_REDIS_TTL;

@Getter
@RedisHash(value = "token", timeToLive = REFRESH_TOKEN_REDIS_TTL)
public class Token {
    @Id
    private String id;

    @Indexed
    private String accessToken;

    private String refreshToken;

    private Token(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static Token of(String accessToken, String refreshToken) {
        return new Token(accessToken, refreshToken);
    }
}
