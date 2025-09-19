package sd_009.bookstore.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import sd_009.bookstore.util.mapper.misc.ErrorMapper;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuthFailureHandler oAuthFailureHandler;
    private final OAuthSuccessHandler oAuthSuccessHandler;
    private final ErrorMapper errorMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorize"))
                        .redirectionEndpoint(redirect -> redirect
                                .baseUri("/login/oauth2/code/google"))
                        .successHandler(oAuthSuccessHandler)
                        .failureHandler(oAuthFailureHandler))
                .exceptionHandling(exHandler -> exHandler
                        .defaultAuthenticationEntryPointFor(((request, response, authException) -> {
                            log.error("Auth failure ex handler", authException);

                            errorMapper.writeFilterErrorDoc(response, HttpStatus.UNAUTHORIZED.value(), "Unauthorized access", request.getRequestURI());
                        }), request -> request.getRequestURI().startsWith("/api"))
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("Access denied ex handler", accessDeniedException);
                            errorMapper.writeFilterErrorDoc(response, HttpStatus.UNAUTHORIZED.value(), "Access denied", request.getRequestURI());
                        }))
                .build();
    }
}
