package sd_009.bookstore.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import sd_009.bookstore.dto.internal.OAuthPrincipalDetails;
import sd_009.bookstore.dto.jsonApiResource.user.response.AuthObject;
import sd_009.bookstore.entity.user.Role;
import sd_009.bookstore.entity.user.RoleType;
import sd_009.bookstore.entity.user.User;
import sd_009.bookstore.repository.RoleRepository;
import sd_009.bookstore.repository.UserRepository;
import sd_009.bookstore.service.auth.JwtService;
import sd_009.bookstore.util.mapper.UserMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        OAuthPrincipalDetails principalDetails = getPrincipalDetails(oauthToken.getPrincipal());

        String email = principalDetails.email();
        String name = principalDetails.name();
        String oauthId = principalDetails.oauthId();

        User user = userRepository
                .findByEmail(principalDetails.email())
                .map(existingUser -> {
                    if (!existingUser.getIsOauth2User()) {
                        existingUser.setIsOauth2User(true);
                        existingUser.setOauth2Id(oauthId);
                    }
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {

                    User newUser = User.builder()
                            .email(email)
                            .username(email.split("@")[0])
                            .oauth2Id(oauthId)
                            .roles(List.of(roleRepository.findByName(RoleType.ROLE_USER.name()).orElseThrow()))
                            .personName(name)
                            .build();
                    return userRepository.save(newUser);
                });

        String jwtToken = jwtService.generateToken(user.getEmail(), Map.of("roles", user.getRoles().stream().map(Role::getName).toList()));

        AuthObject authResp = UserMapper.mapToAuthResponse(user, jwtToken);

        response.setContentType("application/vnd.api+json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), authResp);
    }

    private OAuthPrincipalDetails getPrincipalDetails(OAuth2User principal) {
        String email;
        String name;
        String oauthId;

        email = Objects.requireNonNull(principal.getAttribute("email"), "No email found");
        name = Objects.requireNonNull(principal.getAttribute("name"), "No name found");
        oauthId = Objects.requireNonNull(principal.getName(), "Oauth2Id not found");

        return new OAuthPrincipalDetails(email, name, oauthId);
    }
}
