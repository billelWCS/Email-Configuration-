package com.wcs.security.config;

import com.wcs.security.enums.RoleName;
import com.wcs.security.models.Role;
import com.wcs.security.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepo;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepo = roleRepository;
    }

    @Override
    public void run(String... args) {
        for (RoleName role : RoleName.values()) {
            roleRepo.save(new Role(null, role));
        }
    }
}
