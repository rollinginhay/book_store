package hn_152.bookstore.dto.response.auth;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Collection;

@Builder
public record AuthResponse(
        String oauthId,
        String email,
        String username,
        Collection<String> roles,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String jwtToken
) {
}
