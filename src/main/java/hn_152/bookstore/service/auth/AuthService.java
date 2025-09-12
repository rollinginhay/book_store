package hn_152.bookstore.service.auth;

import hn_152.bookstore.config.exceptionHanding.exception.BadRequestException;
import hn_152.bookstore.config.exceptionHanding.exception.UnauthorizedException;
import hn_152.bookstore.dto.request.auth.LoginRequest;
import hn_152.bookstore.dto.request.auth.RegisterRequest;
import hn_152.bookstore.dto.response.auth.AuthResponse;
import hn_152.bookstore.entity.user.Role;
import hn_152.bookstore.entity.user.RoleType;
import hn_152.bookstore.entity.user.User;
import hn_152.bookstore.repository.RoleRepository;
import hn_152.bookstore.repository.UserRepository;
import hn_152.bookstore.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> new UnauthorizedException("Invalid Email or Password"));

        String token = jwtService.generateToken(user.getEmail(), Map.of("roles", user.getRoles().stream().map(Role::getName)));

        return UserMapper.mapToAuthResponse(user, token);
    }

    public AuthResponse register(RegisterRequest registerRequest) {
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

        String jwtToken = jwtService.generateToken(savedUser.getEmail(), Map.of("roles", savedUser.getRoles().stream().map(Role::getName)));

        return UserMapper.mapToAuthResponse(savedUser, jwtToken);
    }
}
