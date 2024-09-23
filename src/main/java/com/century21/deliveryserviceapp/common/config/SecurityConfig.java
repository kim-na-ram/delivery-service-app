package com.century21.deliveryserviceapp.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/signup", "/api/users/login").permitAll() // 회원가입과 로그인은 인증 없이 접근 가능
                        .anyRequest().authenticated()  // 그 외의 모든 요청은 인증 필요
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 사용하지 않음

        return http.build();
    }
}