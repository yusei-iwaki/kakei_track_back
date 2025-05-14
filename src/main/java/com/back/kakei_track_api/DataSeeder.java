package com.back.kakei_track_api;

import com.back.kakei_track_api.entity.UserEntity;
import com.back.kakei_track_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... arg) {
        if (userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setEmail("admin@example.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("admin"));
            admin.setUserName("管理者");
            admin.setExternalId(admin.getEmail() + "_0");
            userRepository.save(admin);
        }
    }
}
