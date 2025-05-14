package com.back.kakei_track_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final CustomUserDetailsService customUserDetailsService;
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor("your-256-bit-secret-your-256-bit-secret".getBytes());

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    /**
     * JWTトークンの検証を行う
     *
     * @param jwt JWTトークン
     * @param userDetails ユーザー情報
     * @return トークンが有効かどうか
     */
    public boolean validateToken(String jwt, LoginUserDetails userDetails) {
        return extractEmail(jwt).equals(userDetails.getEmail());
    }

    public String extractJwtFromCookies(HttpServletRequest req) {
        if (req.getCookies() == null) return null;
        for (Cookie cookie : req.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * ユーザーの認証情報をセキュリティコンテキストにセット
     *
     * @param userDetails ユーザーの認証情報
     */
    public void setAuthenticationContext(LoginUserDetails userDetails, HttpServletRequest req) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public boolean authenticateFromToken(String jwt, HttpServletRequest req) {
        LoginUserDetails userDetails = customUserDetailsService.loadUserByEmail(extractEmail(jwt));
        if (!validateToken(jwt, userDetails)) return false;

        setAuthenticationContext(userDetails, req);

        return true;
    }
}
