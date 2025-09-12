package hn_152.bookstore.config.security;

import hn_152.bookstore.util.mapper.misc.FilterErrorWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final FilterErrorWriter filterErrorWriter;
    private final OAuthFailureHandler oAuthFailureHandler;
    private final OAuthSuccessHandler oAuthSuccessHandler;

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
                            filterErrorWriter.writeToResp(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access", request.getRequestURI());
                        }), request -> request.getRequestURI().startsWith("/api"))
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.error("Access denied ex handler", accessDeniedException);
                            filterErrorWriter.writeToResp(response, HttpServletResponse.SC_UNAUTHORIZED, "Access denied", request.getRequestURI());
                        }))
                .build();
    }
}
