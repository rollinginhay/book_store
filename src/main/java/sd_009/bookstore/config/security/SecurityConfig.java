package sd_009.bookstore.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sd_009.bookstore.util.mapper.misc.ErrorMapper;

import java.util.List;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfig()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                                //Allow testing UIs
                                .requestMatchers("/v3/**",
                                        "/api-docs/**",
                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/oauth2/**",
                                        "/login/oauth2/**",
                                        "/error").permitAll()
                                // OAuth2 Endpoints - Must be public
                                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                                // GET Requests - Public access
//                        .requestMatchers(HttpMethod.GET, "/v1/books").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/book/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/genres").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/genre/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/publishers").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/publisher/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/seriess").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/series/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/creators").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/creator/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/campaigns").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/v1/campaign/**").permitAll()
//
//                        // POST Requests - Require EMPLOYEE role
//                        .requestMatchers(HttpMethod.POST, "/v1/book/create").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/v1/genre/create").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/v1/publisher/create").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/v1/series/create").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/v1/creator/create").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/v1/bookDetail/create").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/v1/campaign/create").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/v1/campaignDetail/create").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/v1/user/create").hasAnyRole("EMPLOYEE", "ADMIN")
//
//                        // Reviews - Authenticated users can create
//                        .requestMatchers(HttpMethod.POST, "/v1/review/create").authenticated()
//
//                        // Cart - Authenticated users can manage their own cart
//                        .requestMatchers(HttpMethod.POST, "/v1/cartDetail/create").authenticated()
//
//                        // Receipts - Authenticated users can create orders
//                        .requestMatchers(HttpMethod.POST, "/v1/receipt/create").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/v1/receiptDetail/create").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/v1/paymentDetail/create").authenticated()
//
//                        // PUT Requests - Require EMPLOYEE role
//                        .requestMatchers(HttpMethod.PUT, "/v1/book/update").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/genre/update").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/publisher/update").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/series/update").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/creator/update").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/bookDetail/update").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/campaign/update").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/campaignDetail/update").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/v1/user/update").hasAnyRole("EMPLOYEE", "ADMIN")
//
//                        // Reviews - Users can update their own reviews (controller should verify ownership)
//                        .requestMatchers(HttpMethod.PUT, "/v1/review/update").authenticated()
//
//                        // Cart - Users can update their own cart
//                        .requestMatchers(HttpMethod.PUT, "/v1/cartDetail/update").authenticated()
//
//                        // Receipts - Users can update their own orders (if in pending state)
//                        .requestMatchers(HttpMethod.PUT, "/v1/receipt/update").authenticated()
//                        .requestMatchers(HttpMethod.PUT, "/v1/receiptDetail/update").authenticated()
//                        .requestMatchers(HttpMethod.PUT, "/v1/paymentDetail/update").authenticated()
//
//                        // DELETE Requests - Require ADMIN role
//                        .requestMatchers(HttpMethod.DELETE, "/v1/book/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/genre/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/publisher/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/series/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/creator/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/bookDetail/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/campaign/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/campaignDetail/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/user/**").hasRole("ADMIN")
//
//                        // Reviews - Users can delete their own reviews (controller should verify ownership)
//                        .requestMatchers(HttpMethod.DELETE, "/v1/review/**").authenticated()
//
//                        // Cart - Users can delete their own cart items
//                        .requestMatchers(HttpMethod.DELETE, "/v1/cartDetail/**").authenticated()
//
//                        // Receipts - Only admins can delete receipts
//                        .requestMatchers(HttpMethod.DELETE, "/v1/receipt/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/receiptDetail/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.DELETE, "/v1/paymentDetail/**").hasRole("ADMIN")
//
//                        // Relationship Endpoints (PATCH/POST/DELETE)
//                        .requestMatchers("/v1/book/*/relationships/**").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers("/v1/receipt/*/relationships/**").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers("/v1/receiptDetail/*/relationships/**").hasAnyRole("EMPLOYEE", "ADMIN")
//                        .requestMatchers("/v1/campaign/*/relationships/**").hasAnyRole("EMPLOYEE", "ADMIN")
//
//                        // User cart relationships - Authenticated users
//                        .requestMatchers("/v1/user/*/relationships/cartDetail").authenticated()
//
//                        // User Profile Endpoints - Authenticated users can view their own data
//                        .requestMatchers(HttpMethod.GET, "/v1/users").hasRole("ADMIN") //only admin can see all users
//                        .requestMatchers(HttpMethod.GET, "/v1/user/**").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/v1/receipts").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/v1/receipt/**").authenticated()
//                        .requestMatchers(HttpMethod.GET, "/v1/cartDetail/**").authenticated()

                                // Fallback - Deny all other requests
                                .anyRequest().permitAll()
                )
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


    private CorsConfigurationSource corsConfig() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
