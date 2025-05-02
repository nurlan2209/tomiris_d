package com.diploma.service;
import com.diploma.model.Role;
import com.diploma.model.User;
import com.diploma.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader {

    @Bean
    CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("admin").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.ADMIN);
                admin.setName("admin");
                userRepository.save(admin);
            }

            if (userRepository.findByEmail("user").isEmpty()) {
                User user = new User();
                user.setEmail("user");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRole(Role.USER);
                user.setName("user");
                userRepository.save(user);
            }
        };
    }
}

