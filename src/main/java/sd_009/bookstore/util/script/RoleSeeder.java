package sd_009.bookstore.util.script;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sd_009.bookstore.entity.user.Role;
import sd_009.bookstore.entity.user.RoleType;
import sd_009.bookstore.repository.RoleRepository;

@Component
@RequiredArgsConstructor
@Order(1)
public class RoleSeeder {
    private final RoleRepository roleRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void run() {
        for (RoleType roleDef : RoleType.values()) {
            roleRepository.findByName(roleDef.name()).orElseGet(() -> roleRepository.save(Role.builder().name(roleDef.name()).build()));
        }
    }
}
