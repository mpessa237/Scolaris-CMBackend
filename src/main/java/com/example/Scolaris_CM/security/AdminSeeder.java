package com.example.Scolaris_CM.security;

import com.example.Scolaris_CM.models.Role;
import com.example.Scolaris_CM.models.User;
import com.example.Scolaris_CM.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {
    private final UserRepo userRepo ;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.admin.email}")
    private String adminEmail;

    @Value("${application.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepo.existsByEmail(adminEmail)) {
            log.info("Compte admin bootstrap déjà existant, aucune action nécessaire.");
            return;
        }

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("System");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);
        admin.setActive(true);

        userRepo.save(admin);
        log.info("Compte admin bootstrap créé : {}", adminEmail);
    }
}
