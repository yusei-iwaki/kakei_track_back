package com.back.kakei_track_api.service;

import com.back.kakei_track_api.dto.AuthRequestDto;
import com.back.kakei_track_api.security.CustomUserDetailsService;
import com.back.kakei_track_api.security.JwtUtil;
import com.back.kakei_track_api.security.LoginUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * ログイン処理を行い、JWTトークンを返却
     *
     * @param request ログインリクエストDTO（emailとpassword）
     * @return ログイン成功時にJWTトークンを返すレスポンスDTO
     */
    public String login(AuthRequestDto request) {
        try {
            // ユーザー認証
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        LoginUserDetails userDetails = customUserDetailsService.loadUserByEmail(request.getEmail());

        return jwtUtil.generateToken(userDetails.getEmail());
    }

}
