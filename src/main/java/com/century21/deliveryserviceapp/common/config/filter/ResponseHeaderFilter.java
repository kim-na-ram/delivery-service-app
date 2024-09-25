package com.century21.deliveryserviceapp.common.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Order(2)
@Component
@WebFilter("/api/*")
public class ResponseHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String accessToken = (String) request.getAttribute(AUTHORIZATION);

        if (!Strings.isBlank(accessToken)) {
            response.setHeader(AUTHORIZATION, accessToken);
        }

        filterChain.doFilter(request, response);
    }
}