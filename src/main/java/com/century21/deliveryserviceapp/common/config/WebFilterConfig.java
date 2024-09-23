package com.century21.deliveryserviceapp.common.config;

import com.century21.deliveryserviceapp.user.jwt.JwtAuthenticationFilter;
import com.century21.deliveryserviceapp.user.jwt.JwtAuthorizationFilter;
import com.century21.deliveryserviceapp.user.jwt.JwtUtil;
import com.century21.deliveryserviceapp.user.repository.UserRepository;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebFilterConfig {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public WebFilterConfig(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    // JwtAuthenticationFilter 등록
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(jwtUtil, userRepository);

        registrationBean.setFilter(authenticationFilter);
        registrationBean.addUrlPatterns("/api/*"); // 필터가 적용될 URL 패턴 (로그인 및 회원가입은 필터 내부에서 제외)
        return registrationBean;
    }

    // JwtAuthorizationFilter 등록
    @Bean
    public FilterRegistrationBean<JwtAuthorizationFilter> jwtAuthorizationFilter() {
        FilterRegistrationBean<JwtAuthorizationFilter> registrationBean = new FilterRegistrationBean<>();
        JwtAuthorizationFilter authorizationFilter = new JwtAuthorizationFilter(jwtUtil);

        registrationBean.setFilter(authorizationFilter);
        registrationBean.addUrlPatterns("/api/*"); // 필터가 적용될 URL 패턴 (로그인 및 회원가입은 필터 내부에서 제외)
        return registrationBean;
    }
}