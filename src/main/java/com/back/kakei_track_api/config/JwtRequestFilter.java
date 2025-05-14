package com.back.kakei_track_api.config;

import com.back.kakei_track_api.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws ServletException, IOException {

        String jwt = jwtUtil.extractJwtFromCookies(req);

        if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            jwtUtil.authenticateFromToken(jwt, req);
        }

        chain.doFilter(req, res);
    }
}
