package com.back.kakei_track_api;

import com.back.kakei_track_api.entity.UserEntity;
import com.back.kakei_track_api.security.LoginUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/me")
    public UserEntity getMe(@AuthenticationPrincipal LoginUserDetails userdetails) {
        return userdetails.getUser();
    }
}
