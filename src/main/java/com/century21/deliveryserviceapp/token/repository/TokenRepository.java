package com.century21.deliveryserviceapp.token.repository;

import com.century21.deliveryserviceapp.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, String> {
    boolean existsByAccessToken(String accessToken);
    Optional<Token> findByAccessToken(String accessToken);
    void deleteByAccessToken(String accessToken);
}
