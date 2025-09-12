package hn_152.bookstore.util.script;

import hn_152.bookstore.entity.user.Role;
import hn_152.bookstore.entity.user.RoleType;
import hn_152.bookstore.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        for (RoleType roleDef : RoleType.values()) {
            roleRepository.findByName(roleDef.name()).orElseGet(() -> roleRepository.save(Role.builder().name(roleDef.name()).build()));
        }
    }
}
