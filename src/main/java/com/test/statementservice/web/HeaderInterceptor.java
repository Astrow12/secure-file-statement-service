package com.test.statementservice.web;

import com.test.statementservice.exception.JwtMissingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.HandlerInterceptor;

@AllArgsConstructor
@Slf4j
public class HeaderInterceptor implements HandlerInterceptor {

    private final UserStore userStore;

    @Override
    public boolean preHandle(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse, final Object handler) throws Exception {

        log.info("header interceptor has intercepted the following uri: {}", servletRequest.getRequestURI());
        // Optional: get raw token from Authorization header
        String authHeader = servletRequest.getHeader("Authorization");
        String rawToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            rawToken = authHeader.substring(7);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            userId = jwt.getClaimAsString("sub"); // Supabase user ID
            userStore.setUserId(userId);
        } else {
            throw new JwtMissingException(HttpStatus.UNAUTHORIZED, "JWT token missing or invalid");
        }

        // Optional: logging
        System.out.println("Bearer token: " + rawToken);
        System.out.println("User ID: " + userId);

        return true; // continue processing
    }

}
