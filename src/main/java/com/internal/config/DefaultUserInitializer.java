package com.internal.config;

import com.internal.enumation.RoleEnum;
import com.internal.enumation.StatusData;
import com.internal.feature.auth.models.Role;
import com.internal.feature.auth.models.UserEntity;
import com.internal.feature.auth.repository.RoleRepository;
import com.internal.feature.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultUserInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.default-users.create:true}")
    private boolean createDefaultUsers;

    @Value("${app.super.username:admin@gmail.com}")
    private String superCard;

    @Value("${app.super.password:88889999}")
    private String superPassword;

    @Value("${app.super.email:admin@gmail.com}")
    private String superEmail;

    @Override
    public void run(String... args) {
        log.info("Initializing default roles and users...");

        initializeRoles();

        if (createDefaultUsers) {
            createDefaultSuperAdminUser();
        } else {
            log.info("Default user creation is disabled");
        }

        log.info("Role and user initialization completed");
    }

    private void initializeRoles() {
        Arrays.stream(RoleEnum.values()).forEach(roleEnum -> {
            if (!roleRepository.existsByName(roleEnum)) {
                Role role = new Role();
                role.setName(roleEnum);
                roleRepository.save(role);
                log.info("Created role: {}", roleEnum);
            }
        });
    }

    private void createDefaultSuperAdminUser() {
        createDefaultUser(superCard, superEmail, superPassword, RoleEnum.ADMIN);
        createDefaultUser("staff", "staff@gmail.com", "staff123", RoleEnum.STAFF);
        createDefaultUser("customer", "customer@gmail.com", "customer123", RoleEnum.CUSTOMER);
    }

    private void createDefaultUser(String username, String email, String password, RoleEnum roleEnum) {
        if (userRepository.existsByUsername(username)) {
            log.info("User already exists: {}", username);
            return;
        }

        Role role = roleRepository.findByName(roleEnum)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleEnum));

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setStatus(StatusData.ACTIVE);
        user.setPassword(passwordEncoder.encode(password));

        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
        log.info("Created default user: {} with role: {}", username, roleEnum);
    }
}