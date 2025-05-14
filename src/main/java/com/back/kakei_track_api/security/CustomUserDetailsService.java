package com.back.kakei_track_api.security;

import com.back.kakei_track_api.entity.UserEntity;
import com.back.kakei_track_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public LoginUserDetails loadUserByEmail(String email) {
        return loadUserByUsername(email);
    }

    @Override
    public LoginUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new LoginUserDetails(user);
    }
}
