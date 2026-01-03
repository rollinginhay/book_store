package sd_009.bookstore.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sd_009.bookstore.config.exceptionHanding.exception.BadRequestException;
import sd_009.bookstore.config.exceptionHanding.exception.UnauthorizedException;
import sd_009.bookstore.dto.jsonApiResource.user.AuthObject;
import sd_009.bookstore.dto.jsonApiResource.user.LoginRequest;
import sd_009.bookstore.dto.jsonApiResource.user.RegisterRequest;
import sd_009.bookstore.entity.user.Role;
import sd_009.bookstore.entity.user.RoleType;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.repository.RoleRepository;
import sd_009.bookstore.repository.UserRepository;
import sd_009.bookstore.util.mapper.user.UserMapperManual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthObject login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new UnauthorizedException("Invalid Email or Password"));

        String token = jwtService.generateToken(user.getEmail(), Map.of("roles", user.getRoles().stream().map(Role::getName).toList()));

        return UserMapperManual.mapToAuthResponse(user, token);
    }

    public AuthObject loginOnline(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new UnauthorizedException("Invalid Email or Password"));

        // Tạo claims và thêm userId
        Map<String, Object> claims = new HashMap<>();
        System.out.println("User ID: " + user.getId());
        claims.put("id", user.getId());

        claims.put("roles", user.getRoles().stream()
                .map(Role::getName)
                .toList()
        );

        String token = jwtService.generateTokenOnline(user.getEmail(), claims);

        return UserMapperManual.mapToAuthResponse(user, token);
    }


    public AuthObject register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new BadRequestException("Email already exists");
        }

        User newUser = User.builder()
                .email(registerRequest.email())
                .username(registerRequest.email().split("@")[0])
                .password(passwordEncoder.encode(registerRequest.password()))
                .phoneNumber(registerRequest.phoneNumber())
                .roles(List.of(roleRepository.findByName(RoleType.ROLE_USER.name()).get()))
                .build();

        User savedUser = userRepository.save(newUser);

        String jwtToken = jwtService.generateToken(savedUser.getEmail(), Map.of("roles", savedUser.getRoles().stream().map(Role::getName).toList()));

        return UserMapperManual.mapToAuthResponse(savedUser, jwtToken);
    }
}
