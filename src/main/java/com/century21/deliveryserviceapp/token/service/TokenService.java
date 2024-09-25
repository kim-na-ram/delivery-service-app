package com.century21.deliveryserviceapp.token.service;

import com.century21.deliveryserviceapp.entity.Token;
import com.century21.deliveryserviceapp.token.repository.TokenRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public void saveToken(@NonNull String accessToken, @NonNull String refreshToken) {
        Token token = Token.of(accessToken, refreshToken);
        tokenRepository.save(token);
    }

    public boolean isExistRefreshToken(String accessToken) {
        return tokenRepository.existsByAccessToken(accessToken);
    }

    public Token findByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken).get();
    }

    public void republishToken(Token token, String newAccessToken, String newRefreshToken) {
        tokenRepository.delete(token);

        Token newToken = Token.of(newAccessToken, newRefreshToken);
        tokenRepository.save(newToken);
    }

    public void deleteToken(String accessToken) {
        tokenRepository.deleteByAccessToken(accessToken);
    }
}
