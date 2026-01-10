package sd_009.bookstore.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sd_009.bookstore.entity.user.Role;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.repository.UserRepository;
import sd_009.bookstore.service.auth.JwtService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        if (jwtService.validateToken(token)) {
            try {
                String email = jwtService.extractSubject(token);
                log.debug("Extracted email from token: {}", email);

                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isEmpty()) {
                    log.error("User not found with email from token: {}", email);
                    // Không set authentication nếu user không tồn tại
                    filterChain.doFilter(request, response);
                    return;
                }

                User user = userOpt.get();
                List<String> roles = user.getRoles().stream().map(Role::getName).toList();

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, roles.stream().map(SimpleGrantedAuthority::new).toList());

                authToken.setDetails(new WebAuthenticationDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Authentication set for user: {}", email);
            } catch (Exception e) {
                log.error("Error processing JWT token: {}", e.getMessage(), e);
                // Không set authentication nếu có lỗi
            }
        }
        filterChain.doFilter(request, response);
    }
}
