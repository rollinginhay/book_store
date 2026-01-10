package sd_009.bookstore.util.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sd_009.bookstore.config.exceptionHanding.exception.UnauthorizedException;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    /**
     * Lấy email của user hiện tại từ SecurityContext
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Authentication is null or not authenticated");
            throw new UnauthorizedException("User chưa đăng nhập");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String) {
            String email = (String) principal;
            log.debug("Extracted email from SecurityContext: {}", email);
            return email;
        }
        log.warn("Principal is not a String: {}", principal != null ? principal.getClass().getName() : "null");
        throw new UnauthorizedException("Không thể lấy email từ SecurityContext");
    }

    /**
     * Lấy userId của user hiện tại từ SecurityContext
     * Lấy email từ SecurityContext, sau đó query User để lấy ID
     */
    public Long getCurrentUserId() {
        String email = getCurrentUserEmail();
        log.debug("Looking up user by email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            log.error("User not found with email: {}", email);
            throw new UnauthorizedException("User không tồn tại với email: " + email);
        }
        User user = userOpt.get();
        log.debug("Found user with ID: {}", user.getId());
        return user.getId();
    }

    /**
     * Lấy User entity của user hiện tại từ SecurityContext
     */
    public User getCurrentUser() {
        String email = getCurrentUserEmail();
        log.debug("Looking up user by email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            log.error("User not found with email: {}", email);
            throw new UnauthorizedException("User không tồn tại với email: " + email);
        }
        return userOpt.get();
    }
}
