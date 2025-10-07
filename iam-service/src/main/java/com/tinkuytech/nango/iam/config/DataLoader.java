package com.tinkuytech.nango.iam.config;

import com.tinkuytech.nango.iam.entity.Role;
import com.tinkuytech.nango.iam.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("CONDUCTOR").isEmpty()) {
            roleRepository.save(new Role("CONDUCTOR"));
        }
        if (roleRepository.findByName("PASAJERO").isEmpty()) {
            roleRepository.save(new Role("PASAJERO"));
        }
    }
}
